import sys

def read(xs, pos, length):
    ys = xs + xs
    pos = pos % len(xs)
    return ys[pos:pos+length]

def write(xs, pos, ys):
    for i, y in enumerate(ys):
        xs[(i + pos) % len(xs)] = y

lengths = map(int, sys.stdin.readline().split(','))

xs = range(256)
skip = 0
pos = 0
for length in lengths:
    assert length < 256
    write(xs, pos, reversed(read(xs, pos, length)))
    pos += length + skip
    skip += 1

print xs[0] * xs[1]
