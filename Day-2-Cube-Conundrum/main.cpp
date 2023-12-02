#include <iostream>
#include <fstream>
#include <sstream>

using namespace std;

static constexpr int MAX_RED = 12;
static constexpr int MAX_GREEN = 13;
static constexpr int MAX_BLUE = 14;

bool is_valid_step(int red_count, int green_count, int blue_count) {
    return red_count <= MAX_RED && green_count <= MAX_GREEN && blue_count <= MAX_BLUE;
}

bool is_valid_step(const string& step, int& red_count, int& green_count, int& blue_count) {
    stringstream step_ss(step);
    string sub_step;
    red_count = 0, green_count = 0, blue_count = 0;
    while (getline(step_ss, sub_step, ',')) {
        stringstream sub_step_ss(sub_step);
        int count;
        string color;
        sub_step_ss >> count >> color;
        if (color == "red") {
            red_count = count;
        } else if (color == "green") {
            green_count = count;
        } else {
            blue_count = count;
        }
    }
    return is_valid_step(red_count, green_count, blue_count);
}

bool is_valid_game(const string& game, int& power) {
    stringstream game_ss(game);
    string step;
    bool valid = true;
    int max_red = 0, max_green = 0, max_blue = 0;
    while (getline(game_ss, step, ';')) {
        int red_count, green_count, blue_count;
        bool is_valid = is_valid_step(step, red_count, green_count, blue_count);
        valid = valid && is_valid;
        max_red = max(max_red, red_count);
        max_green = max(max_green, green_count);
        max_blue = max(max_blue, blue_count);
    }
    power = max_red * max_green * max_blue;
    return valid;
}

int main(int argc, char** argv) {
    ifstream ifs(argv[1]);
    string line;
    int ans = 0;
    int power_ans = 0ll;
    while (getline(ifs, line)) {
        stringstream line_ss(line);
        int id;
        string skip, game;
        line_ss >> skip >> id >> skip;
        getline(line_ss, game);
        int power;
        if (is_valid_game(game, power)) {
            ans += id;
        }
        power_ans += power;
    }
    cout << ans << endl;
    cout << power_ans << endl;
    return 0;
}
