import sys

step = int(sys.argv[1])
if len(sys.argv) > 2:
    insertions = int(sys.argv[2])
else:
    insertions = 50000000

answer = None
pos = 0
for i in range(1, insertions + 1):
    pos += step
    pos %= i
    if pos == 0:
        answer = i
    pos += 1

print answer
