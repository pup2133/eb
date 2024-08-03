import { ref, reactive} from "vue";

export const calledNumbers = reactive(new Map);
export const gameState = ref('ready');

//게임 진행자 class
export class Manager {
    constructor(number) {
        this.number = number;
    }

    players = [];

    registerPlayer(player) {
        this.players.push(player);
    }

    removePlayer(player) {
        this.players = this.players.filter((p) => p !== player);
    }

    notifyPlayers(winner) {
        for (const player of this.players) {
            player.check(this.number, this.removePlayer.bind(this), winner);
            if (winner.length===2) {
                alert("게임 종료");
                gameState.value = "end";
                return;
            }
        }
    }

    calling(callNum, winner) {
        if (calledNumbers.size === 0) return;
        this.number = callNumber(randomNumber(1, 15));
        callNum.value = this.number;
        this.notifyPlayers(winner);
    }

    gameOver(){

    }
}

// min ~ max 번호 랜덤생성
export const randomNumber = (min, max) => {
    return Math.floor(Math.random() * max) + min;
};

// 랜덤으로 생성된 number가 map.get()으로 가져와지면 불렸던 번호가 아니므로
// map에 number값을 삭제하고 number를 반환
// map에 없으면 중복된 번호이므로 메서드 재실행
const callNumber = (number) => {
    console.log(calledNumbers)
    if (calledNumbers.get(number) === undefined) {
        return callNumber(randomNumber(1, 15));
    } else {
        calledNumbers.delete(number);
        return number;
    }
};