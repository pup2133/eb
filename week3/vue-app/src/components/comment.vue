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
import {instance} from "@/modules/axios";

const props = defineProps({
  postState: {
    type: Object,
    required: true
  },
});

// 댓글 저장
const saveComment = async () => {
  try {
    const post = props.postState;

    const comment = {
      commentWriter: post.commentWriter,
      commentContent: post.commentContent,
      postId: post.view.postId
    };

    const response = await instance.post("/view/save-comment", comment);

    updateComments(response.data.object);

  } catch (error) {
  }
};

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