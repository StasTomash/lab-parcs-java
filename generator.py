import random

INF = 1000000000000000000


def gen(n, path):
    file = open(path, "w")
    file.write(f"{n}\n")
    table = []
    for i in range(n):
        row = []
        for j in range(n):
            row.append(random.randrange(10))
        table.append(row)
        file.write(f"{' '.join(list(map(str, row)))}\n")
    file.close()
    return table


def gen_expected(table, path):
    file = open(path, "w")
    dp = []
    n = len(table)
    for i in range(n):
        dp_row = []
        for j in range(n):
            val_up = INF
            if i > 0:
                val_up = dp[i - 1][j]
            val_left = INF
            if j > 0:
                val_left = dp_row[j - 1]
            if val_up == INF and val_left == INF:
                val_up = 0
            dp_row.append(table[i][j] + min(val_up, val_left))
        dp.append(dp_row)
    file.write(str(dp[n - 1][n - 1]))
    file.close()


if __name__ == '__main__':
    table = gen(50, "out/test.txt")
    gen_expected(table, "expected_output.txt")
