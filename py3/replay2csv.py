import replay_pb2
import csv
import sys
import argparse

def open_out(fn):
    return open(fn, mode='w', encoding='utf8', newline='')

parser = argparse.ArgumentParser(description="Converts a binary .replay file to a (somewhat) human-readable CSV file.")
parser.add_argument('infile', type=argparse.FileType(mode='rb'))
parser.add_argument('outfile', type=open_out)
args = parser.parse_args()

fieldnames = ['time', 'forward_axis', 'horizontal_axis', 'turn_axis']

replay = replay_pb2.Replay()
replay.ParseFromString(args.infile.read())
args.infile.close()

writer = csv.DictWriter(args.outfile, fieldnames=fieldnames)

if replay.replay_frequency == 0:
    replay.replay_frequency = 30.0

for timestep, state in enumerate(replay.state):
    writer.writerow({
        'time': float(timestep/replay.replay_frequency),
        'forward_axis': state.forward_axis,
        'horizontal_axis': state.horizontal_axis,
        'turn_axis': state.turn_axis
    })

args.outfile.close()
