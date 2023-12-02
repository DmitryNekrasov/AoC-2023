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

bool is_valid_step(const string& step) {
    stringstream step_ss(step);
    string sub_step;
    int red_count = 0, green_count = 0, blue_count = 0;
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

bool is_valid_game(const string& game) {
    stringstream game_ss(game);
    string step;
    bool valid = true;
    while (getline(game_ss, step, ';')) {
        valid = valid && is_valid_step(step);
    }
    return valid;
}

int main(int argc, char** argv) {
    ifstream ifs(argv[1]);
    string line;
    int ans = 0;
    while (getline(ifs, line)) {
        stringstream line_ss(line);
        int id;
        string skip, game;
        line_ss >> skip >> id >> skip;
        getline(line_ss, game);
        if (is_valid_game(game)) {
            ans += id;
        }
    }
    cout << ans << endl;
    return 0;
}
