import sys

def cycle_time(range):
    return 2 * (range - 1)

def caught(depth, range, delay = 0):
    return (depth + delay) % cycle_time(range) == 0

def caught_by_any(scanners, delay = 0):
    for (depth, range) in scanners:
        if caught(depth, range, delay):
            return True
    return False

scanners = []
for line in sys.stdin.readlines():
    (depth, range) = map(int, line.strip().split(': '))
    scanners.append((depth, range))

delay = 0
while caught_by_any(scanners, delay):
    delay += 1
print delay
