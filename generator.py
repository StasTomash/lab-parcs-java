import random

GRID_SIZE = 1000000000
MIN_RADIUS = 1000000
MAX_RADIUS = 10000000


def gen(n, path):
    file = open(path, "w")
    file.write(f"{n}\n")
    for i in range(n):
        type = random.randint(1, 2)
        radius = random.randint(MIN_RADIUS, MAX_RADIUS)
        x = random.randint(radius, GRID_SIZE - radius)
        y = random.randint(radius, GRID_SIZE - radius)
        file.write(f"{type} {x} {y} {radius}\n")
    file.close()


if __name__ == '__main__':
    gen(20, "out/test.txt")
