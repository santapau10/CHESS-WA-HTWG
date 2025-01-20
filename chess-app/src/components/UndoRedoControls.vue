<template>
  <div class="controls-container">
    <button
        @click="handleUndo"
        class="control-btn"
        :disabled="!canUndo"
    >
      Undo
    </button>
    <button
        @click="handleRedo"
        class="control-btn"
        :disabled="!canRedo"
    >
      Redo
    </button>
  </div>
</template>

<script>
const API_BASE_URL = process.env.VUE_APP_BACKEND_URL;

export default {
  name: "UndoRedoControls",
  data() {
    return {
      canUndo: false,
      canRedo: false
    };
  },
  methods: {
    async handleUndo() {
      try {
        const response = await fetch(`${API_BASE_URL}/undo`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          mode: 'cors'
        });

        if (response.ok) {
          this.$emit('move-made');
          await this.updateButtonStates();
        }
      } catch (error) {
        console.error('Error during undo:', error);
      }
    },
    async handleRedo() {
      try {
        const response = await fetch(`${API_BASE_URL}/redo`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          mode: 'cors'
        });

        if (response.ok) {
          this.$emit('move-made');
          await this.updateButtonStates();
        }
      } catch (error) {
        console.error('Error during redo:', error);
      }
    },
    async updateButtonStates() {
      try {
        const response = await fetch(`${API_BASE_URL}/undoRedoState`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          },
          mode: 'cors'
        });

        if (response.ok) {
          const data = await response.json();
          this.canUndo = data.canUndo;
          this.canRedo = data.canRedo;
        }
      } catch (error) {
        console.error('Error fetching undo/redo state:', error);
      }
    }
  },
  mounted() {
    this.updateButtonStates();
  }
};
</script>

<style scoped>
.controls-container {
  position: absolute;
  top: 20px;
  right: 20px;
  display: flex;
  gap: 10px;
  z-index: 10;
}

.control-btn {
  padding: 8px 16px;
  background-color: #4a5568;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.control-btn:hover:not(:disabled) {
  background-color: #2d3748;
}

.control-btn:disabled {
  background-color: #718096;
  cursor: not-allowed;
  opacity: 0.7;
}
</style>