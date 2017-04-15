import numpy as np
import math
import cv2
import sys

#
# Vision Target Specs:
#
# There are two vision targets on either side of the peg:
#
# |X|   |X|
# |X|   |X|
# |X| O |X|
# |X|   |X|
# |X|   |X|
#
# Each target is a 2" by 5" (about ~5 cm by ~13 cm) rectangle.
# They are located about 10+3/4 in. up from the carpet,
#  and spaced about 10+1/4 in. away from each other
#  (measuring between the outside edges of the tape)
#

# found on chiefdelphi, for the LifeCam HD-3000

# Empirically found, for the LiveCam Studio:
# Horizontal FOV (radians): 0.776417
# Vertical FOV (radians): 0.551077

# Found on ChiefDelphi, for the LifeCam HD-3000:
# Horizontal FOV (degrees): 60
# Vertical FOV (degrees): 34.3

# Emprically found for the LifeCam HD-3000:
# Horizontal FOV (radians): 0.623525
# Vertical FOV (radians): 0.594530

fovHoriz = 0.623525
fovVert = 0.594530

# Both are in inches:
targetWidth = 2.0 # inches
targetHeight = 5.0

def getTargetDistance(ct, imgWidth, imgHeight):
    min_rect = cv2.minAreaRect(ct)
    pts = np.int0(cv2.boxPoints(min_rect))
    min_width = abs(pts[0][0] - pts[2][0])
    min_height = abs(pts[0][1] - pts[2][1])

    bb = cv2.boundingRect(ct)
    bb_width = bb[2]
    bb_height = bb[3]

    return targetWidth * imgWidth / (bb_width * math.tan(fovHoriz))
    #return targetHeight * imgHeight / (bb_height * math.tan(fovVert))

def getTargetCenter(ct):
    moments = cv2.moments(ct)
    center = (int(moments['m10']/moments['m00']), int(moments['m01']/moments['m00']))

    return center

def getTargetOffset(ct, imgWidth, dist):
    center = getTargetCenter(ct)

    # positive values = target is right/under of center
    centerOffsetHoriz = center[0] - (imgWidth/2.0)

    return centerOffsetHoriz * ((dist*math.tan(fovHoriz)) / imgWidth)

def getFOVAngles(ct, imgWidth, imgHeight, dist):
    min_rect = cv2.minAreaRect(ct)
    pts = np.int0(cv2.boxPoints(min_rect))
    min_width = abs(pts[0][0] - pts[2][0])
    min_height = abs(pts[0][1] - pts[2][1])

    bb = cv2.boundingRect(ct)
    bb_width = bb[2]
    bb_height = bb[3]

    return (
        math.atan2(targetWidth*imgWidth, bb_width*dist),
        math.atan2(targetHeight*imgHeight, bb_height*dist)
    )

smoothing_constant = 0.25
filteredDist = 0
filteredOffset = 0
filteredFOVVert = 0
filteredFOVHoriz = 0

cam = cv2.VideoCapture(1)

if not cam.isOpened():
    print("Error: could not open camera 1")
    sys.exit(-1)

while True:
    ret, frame = cam.read()

    filtered = cv2.boxFilter(frame, -1, (7,7))

    # Filter by HSV:
    filtered = cv2.cvtColor(filtered, cv2.COLOR_BGR2HLS)
    filtered = cv2.inRange(filtered, np.array([0, 94, 0]), np.array([60, 255, 255])) # Green targets (actual)
    #filtered = cv2.inRange(filtered, np.array([90, 0, 105]), np.array([180, 255, 231])) # Red targets (testing)

    _, contours, _ = cv2.findContours(filtered, cv2.RETR_LIST, cv2.CHAIN_APPROX_NONE)

    scoredContours = []
    out = filtered
    out = cv2.cvtColor(out, cv2.COLOR_GRAY2RGB)

    for ct in contours:
        ct_area = cv2.contourArea(ct)

        if ct_area < 100:
            continue

        min_rect = cv2.minAreaRect(ct)
        pts = np.int0(cv2.boxPoints(min_rect))
        min_width = abs(pts[0][0] - pts[2][0])
        min_height = abs(pts[0][1] - pts[2][1])

        moments = cv2.moments(ct)
        center = (int(moments['m10']/moments['m00']), int(moments['m01']/moments['m00']))

        if min_width > 550:
            continue

        # Aspect Ratio test
        # Ideal AS = (2/5)


        #print(pts)

        cv2.circle(out, tuple(pts[0]), 2, (0, 255, 0))
        cv2.circle(out, tuple(pts[2]), 2, (255, 0, 0))

        AS = min_width / min_height #bb[2] / bb[3]

        # Coverage area (Bounding Box area vs particle area) test:
        # Ideal Cvg. area ratio = 1

        bb_area = min_width*min_height
        if bb_area < 10:
            continue

        cvg_ratio = ct_area / bb_area
        cvg_score = 100/math.exp(abs(cvg_ratio-1))

        cv2.putText(out, str.format("{:.3g}", cvg_ratio), tuple(pts[0]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 0))
        cv2.putText(out, str.format("{:.3g}", AS), tuple(pts[2]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255))

        if AS < 0.1 or AS > 0.6:
            cv2.drawContours(out, [pts], 0, (0, 0, 255), 4)
            continue
        else:
            cv2.drawContours(out, [pts], 0, (0, 255, 0), 4)

        as_score = 100/math.exp(abs(AS-(2/5)))

        scoredContours.append((ct, cvg_score+as_score))

    scoredContours.sort(key=lambda ct: ct[1])

    if len(scoredContours) >= 2:
        tgt1 = scoredContours.pop()
        tgt2 = scoredContours.pop()

        # Calculate distance to targets:
        #print(frame.shape)
        dist1 = getTargetDistance(tgt1[0], frame.shape[1], frame.shape[0])
        dist2 = getTargetDistance(tgt2[0], frame.shape[1], frame.shape[0])
        print(str.format("d1: {:.3f} inches", dist1))
        print(str.format("d2: {:.3f} inches", dist2))

        # Get FOV angles:
        angles1 = getFOVAngles(tgt1[0], frame.shape[1], frame.shape[0], 72.0)
        angles2 = getFOVAngles(tgt2[0], frame.shape[1], frame.shape[0], 72.0)

        avgFOVHoriz = (angles1[0]+angles2[0])/2.0
        avgFOVVert = (angles1[1]+angles2[1])/2.0
        avgDist = (dist1+dist2)/2.0

        # Get target offsets:
        offset1 = getTargetOffset(tgt1[0], frame.shape[1], dist1)
        offset2 = getTargetOffset(tgt2[0], frame.shape[1], dist2)
        avgOffset = (offset1+offset2)/2.0

        filteredFOVHoriz = filteredFOVHoriz + (smoothing_constant*(avgFOVHoriz-filteredFOVHoriz))
        filteredFOVVert = filteredFOVVert + (smoothing_constant*(avgFOVVert-filteredFOVVert))
        filteredDist = filteredDist + (smoothing_constant * (avgDist - filteredDist))
        filteredOffset = filteredOffset + (smoothing_constant * (avgOffset - filteredOffset))

        center1 = getTargetCenter(tgt1[0])
        center2 = getTargetCenter(tgt2[0])

        drawpt1 = (int((center1[0]+center2[0])/2.0)-50, int((center1[1]+center2[1])/2.0)+50)
        drawpt2 = (int((center1[0]+center2[0])/2.0)-50, int((center1[1]+center2[1])/2.0)+70)

        cv2.putText(out, str.format("Distance: {:.3g} inches", filteredDist), drawpt1, cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0))
        cv2.putText(out, str.format("Lateral Offset: {:.3g} inches", filteredOffset), drawpt2, cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0))

        print(str.format("Approx. horiz. FOV: {:f} radians,", filteredFOVHoriz))
        print(str.format("Approx. vert. FOV: {:f} radians,", filteredFOVVert))
        print(str.format("Approx. distance: {:.3f} inches.", filteredDist))
        print(str.format("Approx. lateral offset: {:.3f} inches.", avgOffset))

        out = cv2.drawContours(out, [tgt1[0], tgt2[0]], -1, (255, 0, 0), -1)

    cv2.imshow("Output", out)

    if cv2.waitKey(int(1000/60)) == 27:
        cv2.destroyAllWindows()
        break
