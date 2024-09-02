<template>
  <tr class="comment">
    <td v-for="comment in props.postState.comments" class="comment">
      {{ comment.commentWriter }}
      {{ comment.commentContent }}
      {{ comment.commentCreatedDate }}
    </td>
  </tr>
  <tr>
    <td>
      <input name="write" type="text" v-model="props.postState.commentWriter">
    </td>
    <td>
      <input name="comment" type="text" v-model="props.postState.commentContent">
    </td>
    <td>
      <button type="button" @click="saveComment">등록</button>
    </td>
  </tr>
</template>

<script setup>
import {defineProps} from 'vue';
import boardService from "@/service/boardService";

/* props */
const props = defineProps({
  postState: {
    type: Object,
    required: true
  },
});

/**
 * 1. boardService.saveComment(comment)를 호출하여 받아온 데이터로
 * 2. props.postState 상태 변경
 * @returns {Promise<void>}
 */
const saveComment = async () => {
  try {
    const comment = {
      commentWriter: props.postState.commentWriter,
      commentContent: props.postState.commentContent,
      postId: props.postState.view.postId
    };

    const response = await boardService.saveComment(comment);

    updateComments(response.data.object);
  } catch (error) {
  }
};

/**
 * 1. props.postSate 상태 변경
 * @param comment
 */
const updateComments = (comment) => {
  props.postState.comments.push({
    commentWriter: comment.commentWriter,
    commentContent: comment.commentContent,
    commentCreatedDate: comment.commentCreatedDate
  });

  props.postState.commentWriter = "";
  props.postState.commentContent = "";
}
</script>

<style scoped>
.comment {
  display: flex;
  flex-direction: column;
}

.comment td {
  padding: 4px;
}
</style>