import sys

registers = map(int, sys.stdin.readlines())
p = 0
jumps = 0
while 0 <= p < len(registers):
    old_p = p
    p += registers[p]
    registers[old_p] += 1
    jumps += 1
print jumps
