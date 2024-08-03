// 게임 참가자 class
export class Player {
    constructor(id, bingo, state) {
        this.id = id;
        this.bingo = bingo;
        this.state = state;
    }

    // 불린 번호와 player.bingo.number가 같으면 bingo.done을 true로 변경
    // bingoCheck를 해서 player.state 변경
    // state가 ture
    check(number, removePlayer, winner) {
        this.bingo.forEach((bingo) => {
            if (bingo.number === number) bingo.done = true;
        });

        this.state = bingoCheck(this.bingo);

        if (this.state) {
            removePlayer(this);
            winner.push(this);
        }
    }
}

//빙고가 됐는지 체크,
const bingoCheck = (bingo) => {
    const bingoPatterns = [
        [0, 1, 2],
        [3, 4, 5],
        [6, 7, 8],
        [0, 3, 6],
        [1, 4, 7],
        [2, 5, 8],
        [0, 4, 8],
        [2, 4, 6],
    ];

    return  bingoPatterns.some((pattern) =>
        pattern.every((index) => bingo[index].done)
    );
};
