import sys
import re

def read(xs, pos, length):
    ys = xs + xs
    pos = pos % len(xs)
    return ys[pos:pos+length]

def write(xs, pos, ys):
    for i, y in enumerate(ys):
        xs[(i + pos) % len(xs)] = y

def hash1(lengths, xs, pos, skip):
    xs = xs + [] # Make a copy.
    for length in lengths:
        assert length < 256
        write(xs, pos, reversed(read(xs, pos, length)))
        pos += length + skip
        skip += 1
    return xs, pos, skip

def groups(xs, n):
    "Ignores leftovers."
    xss = []
    for k in range(len(xs) // n):
        xss.append(xs[k * n : (k + 1) * n])
    return xss

def xor_all(xs):
    r = 0
    for x in xs:
        r ^= x
    return r

def knot_hash(key_string):
    lengths = map(ord, key_string) + [17, 31, 73, 47, 23]

    xs = range(256)
    pos = 0
    skip = 0
    for _ in range(64):
        (xs, pos, skip) = hash1(lengths, xs, pos, skip)

    dense = map(xor_all, groups(xs, 16))
    hash = ""
    for value in dense:
        hash += "%02x" % value
    return hash

def hex_digit_to_binary(hex_digit):
    return ('000' + bin(int('0x' + hex_digit, 0))[2:])[-4:]

input = sys.stdin.readline().strip()
key_strings = list(input + "-" + str(d) for d in range(128))
binary = ""
for key_string in key_strings:
    for digit in knot_hash(key_string):
        binary += hex_digit_to_binary(digit)

print len(re.findall('1', binary))
