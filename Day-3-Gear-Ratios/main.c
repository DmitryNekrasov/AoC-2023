#include <stdio.h>
#include <string.h>

#define MAXN 200

char a[MAXN][MAXN];
unsigned n = 0, m = 0;

struct gear {
    int count;
    int product;
};

struct gear gears[MAXN * MAXN];

void init_gears() {
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            gears[i * m + j].count = 0;
            gears[i * m + j].product = 1;
        }
    }
}

int is_digit(char c) {
    return c >= '0' && c <= '9';
}

int char_to_int(char c) {
    return c - '0';
}

int to_int(int row, int start, int end) {
    int result = 0;
    for (int j = start; j < end; j++) {
        result *= 10;
        result += char_to_int(a[row][j]);
    }
    return result;
}

void inject_gear(int i, int j, int value) {
    int index = i * m + j;
    gears[index].count++;
    gears[index].product *= value;
}

int is_symbol(int i, int j, int value) {
    if (i < 0 || i >= n || j < 0 || j >= m) {
        return 0;
    }
    char c = a[i][j];
    if (c == '*') {
        inject_gear(i, j, value);
    }
    return c != '.' && c != '\n';
}

int calculate_for_number(int row, int start, int end) {
    int number = to_int(row, start, end);
    int symbol_count = is_symbol(row, start - 1, number) + is_symbol(row, end, number);
    for (int j = start - 1; j <= end; j++) {
        symbol_count += is_symbol(row - 1, j, number) + is_symbol(row + 1, j, number);
    }
    return symbol_count * number;
}

int solve() {
    int result = 0;
    for (int i = 0; i < n; i++) {
        int start = -1;
        for (int j = 0; j < m; j++) {
            if (is_digit(a[i][j])) {
                if (start == -1) {
                    start = j;
                }
            } else if (start != -1) {
                result += calculate_for_number(i, start, j);
                start = -1;
            }
        }
    }
    return result;
}

int get_gears_result() {
    int result = 0;
    for (int i = 0, ei = n * m; i < ei; i++) {
        if (gears[i].count == 2) {
            result += gears[i].product;
        }
    }
    return result;
}

int main(int argc, char** argv) {
    FILE* fptr = fopen(argv[1], "r");
    while (fgets(a[n], MAXN, fptr)) {
        if (m == 0) {
            m = strlen(a[n]);
        }
        n++;
    }
    init_gears();
    int result = solve();
    printf("%d\n", result);
    int gear_ratios = get_gears_result();
    printf("%d\n", gear_ratios);
    fclose(fptr);
    return 0;
}
