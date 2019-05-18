import sys
import re

def generator(factor, previous):
    while True:
        previous = (previous * factor) % 2147483647
        yield previous

lines = sys.stdin.readlines()
m = re.match(r'Generator A starts with (\d+)\nGenerator B starts with (\d+)', ''.join(lines))
if m is None:
    raise Exception("Invalid input")
start_a = int(m.group(1))
start_b = int(m.group(2))

gen_a = generator(16807, start_a)
gen_b = generator(48271, start_b)

bits = 16
bitmask = 2 ** bits - 1

count = 0
for _ in range(40000000):
    if gen_a.next() & bitmask == gen_b.next() & bitmask:
        count += 1

print count
