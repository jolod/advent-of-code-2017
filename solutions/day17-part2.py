# This version is *a lot* faster than the naive version. For me, 5 billion
# insertions take about 15 seconds with the naive version, as reported by the
# linux `time` command. With this version `time` reports about a tenth of a
# second, which is how long it takes to just start Python and do nothing. In
# fact with 5 quadrillion the program starts and completes in a tenth of a
# second.

# An exponential growth in insertions results in only a linear growth in the
# number of performed iterations. Said differently, running time is O(log(n))
# where n is the number of insertions.

import sys

step = int(sys.argv[1])
if len(sys.argv) > 2:
    insertions = int(sys.argv[2])
else:
    insertions = 50000000

pos = 0
answer = None
insertion = 1
while insertion <= insertions:
    batch = max(1, (insertion - pos) // step)
    pos += batch * (step + 1) - 1
    insertion += batch - 1
    pos %= insertion
    if pos == 0:
        answer = insertion
    pos += 1
    insertion += 1

print answer
