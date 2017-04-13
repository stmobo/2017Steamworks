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

    filtered = cv2.boxFilter(frame, -1, 4)

    # Filter by HSV:
    filtered = cv2.cvtColor(filtered, cv2.BGR2HSV)
    filtered = cv2.inRange(filtered, [8, 119, 214], [36, 214, 255])

    ctx, contours = cv2.findContours(filtered, cv2.RETR_LIST, cv2.CHAIN_APPROX_NONE)

    scoredContours = []
    for ct in contours:
        ct_area = cv2.contourArea(ct)

        if ct_area < 1000 or ct_area > 200000:
            continue

        bb = cv2.boundingRect(ct)

        if bb.width > 400:
            continue

        if bb.width < 25:
            continue

        # Coverage area (Bounding Box area vs particle area) test:
        # Ideal Cvg. area ratio = 1
        bb_area = bb.width * bb.height
        cvg_ratio = ct_area / bb_area # particle area = moment-00
        cvg_score = 100/math.exp(abs(cvg_ratio-1))

        # Aspect Ratio test
        # Ideal AS = (2/5)
        AS = cv2.minAreaRect(ct)
        as_score = 100/math.exp(abs(AS-(2/5)))

        scoredContours.append((ct, cvg_score+as_score))

    scoredContours.sort(key=lambda ct: ct[1])

    tgt1 = scoredContours.pop()
    tgt2 = scoredContours.pop()

    out = filtered.clone()
    out = cv2.cvtColor(out, cv.GRAY2RGB)
    out = cv2.drawContours(out, [tgt1, tgt2], -1, (0, 255, 0), -1)

    cv2.imshow("Output", out)

    if waitKey(1000/60) == 27:
        cv2.destroyAllWindows()
        break
