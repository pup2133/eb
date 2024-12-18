<script setup>
import { ref, reactive } from "vue";
import { Player } from "@/modules/player";
import {
  Manager,
  randomNumber,
  calledNumbers,
  gameState,
} from "@/modules/manager";

const containerClass = ref("container");
const itemClass = ref("item");
const notCheckedClass = ref("notChecked");
const checkedClass = ref("checked");

const players = reactive([]);
const winner = reactive([]);

const playerId = ref(0);
const manager = ref();
const callNum = ref();

//플레이어 추가 addNumber()로 생성된 번호 9개를 Player.bingo에 추가
const addPlayer = () => {
  if (players.length === 5) {
    alert("플레이어는 5명 까지");
    return;
  }

  const set = addNumber();
  const numbers = Array.from(set);
  const bingo = setBingo(numbers);

  const player = new Player(playerId.value++, bingo, false);

  players.push(player);
};

// 1~15의 랜덤 숫자를 set에 9개가 추가될 때 까지 반복
const addNumber = () => {
  const numbers = new Set();
  while (numbers.size < 9) {
    numbers.add(randomNumber(1, 15));
  }
  return numbers;
};

// Player.bingo 생성
const setBingo = (numbers) => {
  const bingo = [];
  for (let i = 0; i < numbers.length; i++) {
    bingo.push({ number: numbers[i], done: false });
  }
  return bingo;
};

// 게임 시작 시 players를 caller에 등록
const gameStart = () => {
  const m = new Manager();

  for (const playersElement of players) {
    m.registerPlayer(playersElement);
  }

  addCalledNumbers();

  manager.value = m;
  gameState.value = "play";
};

// 게임이 시작 될 때 실행
// 번호 중복 방지를 위해 사용 map에 key(1),value(1) 식으로 저장
const addCalledNumbers = () => {
  for (let i = 1; i <= 15; i++) {
    calledNumbers.set(i, i);
  }
};

// 번호 뽑기
const call = () => {
  manager.value.calling(callNum, winner); //-
};

// 게임 재시작을 위해 값 변경
const gameRestart = () => {
  players.length = 0;
  winner.length = 0;
  callNum.value = "";
  gameState.value = "ready";
};
</script>

<template>
  <button v-if="gameState === 'ready'" @click="addPlayer">플레이어 추가</button>
  <button v-if="players.length >= 2 && gameState === 'ready'" @click="gameStart">게임 시작</button>
  <button v-if="gameState === 'play'" @click="call">번호 뽑기</button>
  <button v-if="gameState === 'end'" @click="gameRestart">다시하기</button>
  <h1>{{ callNum }}</h1>

  <div v-for="player in players" :key="player.id" :class="containerClass">
    <div v-for="bingo of player.bingo" :key="bingo.number" :class="itemClass">
      <div v-if="player.state">BINGO</div>
      <div v-else>
        <div v-if="bingo.done" :class="checkedClass">{{ bingo.number }}</div>
        <div v-else :class="notCheckedClass">{{ bingo.number }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  margin: 30px;
  display: grid;
  grid-template-columns: 100px 100px 100px;
  grid-template-rows: 100px 100px 100px;
}

.item {
  text-align: center;
  line-height: 100px;
}

.checked {
  background-color: crimson;
}

.notChecked {
  background-color: white;
}
</style>
