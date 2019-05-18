import sys
import re

def to_poly(p, v, a):
    return [p, v + a / 2.0, a / 2.0]

def poly_add(a, b):
    result = list(a)
    for i in range(len(b)):
        if i == len(result):
            result.append(0)
        result[i] += b[i]
    return result

def poly_inv(p):
    result = list(p)
    for i in range(len(result)):
        result[i] *= -1
    return result

def poly_abs_pos_limit(poly):
    for i in reversed(range(len(poly))):
        if poly == 0:
            continue
        else:
            if poly[i] < 0:
                poly = poly_inv(poly)
            break
    return poly

def dist_poly(polys):
    result = []
    for poly in polys:
        result = poly_add(result, poly_abs_pos_limit(poly))
    return result

particles = []
for line in sys.stdin.readlines():
    (p_x, p_y, p_z, v_x, v_y, v_z, a_x, a_y, a_z) = map(int, re.findall(r'-?\d+', line))
    particles.append([
        to_poly(p_x, v_x, a_x),
        to_poly(p_y, v_y, a_y),
        to_poly(p_z, v_z, a_z),
    ])

dist_polys = [ dist_poly(_) for _ in particles ]

print min(enumerate(dist_polys), key = lambda (_, p): list(reversed(p)))[0]
