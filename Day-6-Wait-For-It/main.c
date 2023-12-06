#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define MAXN 100

FILE* fptr;

int get_values(int* result) {
    char buf[MAXN];
    fgets(buf, MAXN, fptr);
    char* token = strtok(buf, " ");
    token = strtok(NULL, " ");
    int index = 0;
    while (token) {
        int value = atoi(token);
        result[index] = value;
        index++;
        token = strtok(NULL, " ");
    }
    return index;
}

int main(int argc, char** argv) {
    fptr = fopen(argv[1], "r");
    int times[MAXN], distances[MAXN];
    get_values(times);
    int size = get_values(distances);

    int ans = 1;
    for (int test = 0; test < size; test++) {
        int time = times[test], distance = distances[test];
        int test_result = 0;
        for (int i = 0; i <= time; i++) {
            if (i * (time - i) > distance) {
                test_result++;
            }
        }
        ans *= test_result;
    }
    printf("%d", ans);

    fclose(fptr);
    return 0;
}
