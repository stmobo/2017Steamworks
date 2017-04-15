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

cam = cv2.VideoCapture(0)

if not cam.isOpened():
    print("Error: could not open camera 0")
    sys.exit(-1)

while True:
    ret, frame = cam.read()

    filtered = cv2.boxFilter(frame, -1, (7,7))

    # Filter by HSV:
    filtered = cv2.cvtColor(filtered, cv2.COLOR_BGR2HSV)
    #filtered = cv2.inRange(filtered, [8, 119, 214], [36, 214, 255]) # Green targets (actual)
    filtered = cv2.inRange(filtered, np.array([90, 0, 105]), np.array([180, 255, 231])) # Red targets (testing)

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
        cvg_ratio = ct_area / bb_area
        cvg_score = 100/math.exp(abs(cvg_ratio-1))

        cv2.putText(out, str(cvg_ratio), tuple(pts[2]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0))
        cv2.putText(out, str(AS), tuple(pts[0]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255))

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

        out = cv2.drawContours(out, [tgt1[0], tgt2[0]], -1, (255, 0, 0), -1)

    cv2.imshow("Output", out)

    if cv2.waitKey(int(1000/60)) == 27:
        cv2.destroyAllWindows()
        break
