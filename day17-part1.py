import sys

step = int(sys.argv[1])

pos = 0
buffer = [0]

for i in range(1, 2017 + 1):
    pos += step
    pos %= len(buffer)
    pos += 1
    buffer.insert(pos, i)

print buffer[(pos + 1) % len(buffer)]
