#include <stdio.h>
#include <string.h>

#define MAXN 200

char a[MAXN][MAXN];

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

void scan(int n, int m) {
    for (int i = 0; i < n; i++) {
        int start = -1;
        for (int j = 0; j < m; j++) {
            if (is_digit(a[i][j])) {
                if (start == -1) {
                    start = j;
                }
            } else if (start != -1) {
                int result = to_int(i, start, j);
                printf("result = %d\n", result);
                start = -1;
            }
        }
    }
}

int main(int argc, char** argv) {
    FILE* fptr = fopen(argv[1], "r");
    unsigned n = 0, m = 0;
    while (fgets(a[n], MAXN, fptr)) {
        if (m == 0) {
            m = strlen(a[n]);
        }
        n++;
    }
    scan(n, m);
    fclose(fptr);
    return 0;
}
