import sys
import re

lines = sys.stdin.readlines()

for line in lines:
    assert re.match(r' .* $', line)
assert re.match(r' +$', lines[-1])

m = re.search(r'\|', lines[0])
i = 0
j = m.start()

direction = [1, 0]

letters = []
old_char = lines[i][j]
steps = 1
while True:
    i += direction[0]
    j += direction[1]

    char = lines[i][j]

    # print "%d %d %s" % (i, j, char)

    m = re.search(r'\w', char)
    if m:
        letters.append(m.group())
    elif char == '+':
        next_char = lines[i + direction[0]][j + direction[1]]
        assert next_char == ' '
        if old_char == '-':
            if lines[i - 1][j] != ' ':
                direction = [-1, 0]
            elif lines[i + 1][j] != ' ':
                direction = [1, 0]
            else:
                raise Exception()
        elif old_char == '|':
            if lines[i][j - 1] != ' ':
                direction = [0, -1]
            elif lines[i][j + 1] != ' ':
                direction = [0, 1]
            else:
                raise Exception()
    elif char == ' ':
        print ''.join(letters)
        print steps
        break
    else:
        assert char in ['-', '|']
    old_char = char
    steps += 1
