#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define MAXN 100

FILE* fptr;

long long get_values(int* result, int* size) {
    char buf[MAXN], big_value[MAXN];
    int last_copy_index = 0;
    fgets(buf, MAXN, fptr);
    char* token = strtok(buf, " ");
    token = strtok(NULL, " ");
    int index = 0;
    while (token) {
        strcpy(big_value + last_copy_index, token);
        last_copy_index += strlen(token);
        int value = atoi(token);
        result[index] = value;
        index++;
        token = strtok(NULL, " ");
    }
    if (size != NULL) {
        *size = index;
    }
    return atoll(big_value);
}

long long solve(long long time, long long distance) {
    long long test_result = 0;
    for (long long i = 0; i <= time; i++) {
        if (i * (time - i) > distance) {
            test_result++;
        }
    }
    return test_result;
}

int main(int argc, char** argv) {
    fptr = fopen(argv[1], "r");
    int times[MAXN], distances[MAXN], size;
    long long big_time = get_values(times, &size);
    long long big_distance = get_values(distances, NULL);

    int ans_part_1 = 1;
    for (int test = 0; test < size; test++) {
        ans_part_1 *= solve(times[test], distances[test]);
    }
    printf("%d\n", ans_part_1);

    long long ans_part_2 = solve(big_time, big_distance);
    printf("%lld", ans_part_2);

    fclose(fptr);
    return 0;
}
