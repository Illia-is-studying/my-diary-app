document.getElementById('add-tag-button').addEventListener('click', function () {
    const newTag = document.getElementById('new-tag').value.trim();

    if (newTag) {
        const tagAction = document.getElementById('tag-action');
        tagAction.value = 'addtag';
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const buttons = document.querySelectorAll('[data-tag-id]');

    buttons.forEach(button => {
        button.addEventListener('click', (event) => {
            const buttonId = event.currentTarget.getAttribute('data-tag-id');
            const tagIdInput = document.getElementById('tag-id');
            if (tagIdInput) {
                tagIdInput.value = buttonId;
            }

            const tagAction = document.getElementById('tag-action');
            if (tagAction) {
                tagAction.value = 'remove';
            }
        });
    });
});

function setFragmentAction(action) {
    const fragmentAction = document.getElementById('fragment-action');
    fragmentAction.value = action;
}

function autoResizeTextarea(textarea) {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 5 + 'px';
}

function setModalWindowData(buttonElement) {
    const diaryIdValue = buttonElement.getAttribute('data-diary-id');
    const diaryTitleValue = buttonElement.getAttribute('data-diary-title');

    const diaryIdInput = document.getElementById("diaryIdModal");
    const diaryTitleSpan = document.getElementById("diaryTitleSpan");
    const diaryTitleInput = document.getElementById("titleInputModal");

    diaryIdInput.value = diaryIdValue;
    diaryTitleSpan.innerHTML = `<b>${diaryTitleValue}</b>`;
    diaryTitleInput.value = diaryTitleValue;
}