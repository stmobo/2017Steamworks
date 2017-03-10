import replay_pb2
import csv
import sys
import argparse

#
# Usage: csv2replay [infile.csv] [outfile.replay]
#
# [infile] is a CSV file containing joystick input data.
# The first row of [infile] should contain any combination of the following
# column headers:
#  - "time" (must be included)
#  - "forward_axis"
#  - "horizontal_axis"
#  - "turn_axis"
# The corresponding columns should contain the actual values for each axis/button state,
# indexed by time.
#

parser = argparse.ArgumentParser(description="Converts a human-readable CSV file to a binary .replay file.")
parser.add_argument('infile', type=open)
parser.add_argument('outfile', type=argparse.FileType(mode='wb'))
parser.add_argument('-f', '--frequency', dest='frequency', default=30.0, type=float)

args = parser.parse_args()

def row2state(row, state):
    state.forward_axis = float(row.get('forward_axis', 0.0))
    state.horizontal_axis = float(row.get('horizontal_axis', 0.0))
    state.turn_axis = float(row.get('turn_axis', 0.0))

reader = csv.DictReader(args.infile)
r = replay_pb2.Replay()
r.replay_frequency = args.frequency
r.battery_voltage = 12.0

lastTime = 0.0  # in seconds
lastState = {}  # default state

for row in reader:
    if "time" not in row:
        print("ERROR: CSV input must be indexed by time!")
        sys.exit(-1)
    print("[T+{}] Fwd:{} Hrz:{} Trn:{}".format(
        row['time'],
        float(row.get('forward_axis', 0.0)),
        float(row.get('horizontal_axis', 0.0)),
        float(row.get('turn_axis', 0.0)),
    ))
    deltaT = float(row["time"]) - lastTime

    for i in range(int(deltaT*args.frequency)):
        row2state(lastState, r.state.add()) # hold previous input values up to this timestep

    print("[T+{}] Held state for {} timesteps.".format(row["time"], deltaT*args.frequency))

    row2state(row, r.state.add()) # add this as next state

    lastState = row
    lastTime = float(row["time"])

args.infile.close()

args.outfile.write(r.SerializeToString())
args.outfile.close()
