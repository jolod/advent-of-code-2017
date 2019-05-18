import sys

def cycle_time(range):
    return 2 * (range - 1)

severity = 0
for line in sys.stdin.readlines():
    (depth, range) = map(int, line.strip().split(': '))
    if depth % cycle_time(range) == 0:
        severity += depth * range
print severity
