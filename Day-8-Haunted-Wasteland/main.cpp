#include <iostream>
#include <fstream>
#include <sstream>

using namespace std;

pair<string , pair<string, string>> parse_line(const string& line) {
    string from, left, right, skip;
    stringstream ss(line);
    ss >> from >> skip >> left >> right;
    return make_pair(from, make_pair(left.substr(1, 3), right.substr(0, 3)));
}

int main(int argc, char** argv) {
    ifstream ifs(argv[1]);
    string direction;
    getline(ifs, direction);
    string line;
    getline(ifs, line);
    while (getline(ifs, line)) {
        auto res = parse_line(line);
        cout << res.first << " " << res.second.first << " " << res.second.second << endl;
    }

    return 0;
}
