var fileList = document.getElementById('fileList');
var driveStatus = document.getElementById('driveStatus');
var goUpButton = document.getElementById('goUpButton');
goUpButton.onclick = () => goUp();

var currentPath = '';
var input = document.getElementById('fileInput');
input.onchange = () => uploadFile();

var uploadButton = document.getElementById('uploadButton');
uploadButton.onclick = () => input.click();

var createFolderButton = document.getElementById('createFolderButton');
createFolderButton.onclick = showCreateFolderDialog;

var textInputDialog = document.getElementById('textInputDialog');
var textInput = document.getElementById('textInput');
var closeButton = document.getElementById('closeButton');
closeButton.onclick = closeDialog;
var submitButton = document.getElementById('submitButton');
submitButton.onclick = createFolder;

var selectedFile;
var selectedType;

var deleteButton = document.getElementById('deleteButton');
deleteButton.onclick = () => {
    if (selectedType == 'folder') {
        deleteFolder(selectedFile);
    } else {
        deleteFile(selectedFile);
    }
}

function addFile(path, element) {
    var itemElement = document.createElement('div');
    itemElement.classList.add('item');

    var imgContainer = document.createElement('div');
    imgContainer.classList.add('imgContainer');

    var imgElement = document.createElement('img');
    imgElement.src = "files?file=" + path + "/" + element.name;

    var nameElement = document.createElement('span');
    nameElement.classList.add('fileName');
    nameElement.textContent = element.name;

    var dateElement = document.createElement('span');
    dateElement.classList.add('fileDate');
    dateElement.textContent = element.lastModified;

    imgContainer.appendChild(imgElement);

    var optionsElement = createOptionsElement();

    itemElement.appendChild(imgContainer);
    itemElement.appendChild(nameElement);
    itemElement.appendChild(dateElement);
    itemElement.appendChild(optionsElement);

    if (element.folder) {
        imgElement.onerror = () => onFolderPreviewError(imgElement);
        itemElement.classList.add('folder');
        itemElement.onclick = () => {
            loadFolder(currentPath + "/" + element.name);
        };
        optionsElement.onclick = (e) => {
            rightClick(e);
            selectedFile = element.name;
            selectedType = 'folder';
        };
    } else {
        imgElement.onerror = () => onFilePreviewError(imgElement);
        optionsElement.onclick = (e) => {
            rightClick(e);
            selectedFile = element.name;
            selectedType = 'file';
        };
    }

    fileList.appendChild(itemElement);
}

function createOptionsElement() {
    var optionsElement = document.createElement('span');
    optionsElement.classList.add('material-symbols-outlined', 'fileOptions');
    optionsElement.textContent = 'more_vert';
    return optionsElement;
}

function setDriveStatus(color) {
    driveStatus.style.color = color;
}

async function getFolderData(path) {
    const response = await fetch('/folder', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(
            { 
                'path': path 
            }
        )
    });

    const result = await response.json();

    return result;
}

function onFilePreviewError(image) {
    image.src = '/img/file-icon.png';
}

function onFolderPreviewError(image) {
    image.src = '/img/folder-icon.png';
}

async function loadFolder(path) {
    pathArray = path.split('/');

    var result = await getFolderData(pathArray);

    // Sort folders first
    result.files.sort(function(x, y) {
        return (x.folder === y.folder) ? 0 : x.folder ? -1 : 1;
    });

    fileList.innerHTML = '';

    result.files.forEach(file => {
      addFile(result.name, file);
    });

    currentPath = result.name;
    document.getElementById('currentPath').textContent = currentPath;

    if (currentPath == '') {
        goUpButton.classList.add('disabled');
        goUpButton.onclick = null;
    } else {
        goUpButton.classList.remove('disabled');
        goUpButton.onclick = () => goUp();
    }
}

function goUp() {
    var lastSlash = currentPath.lastIndexOf('/');
    currentPath = currentPath.substring(0, lastSlash);

    loadFolder(currentPath);
}

async function uploadFile() {
    var data = new FormData();
    data.append('path', currentPath);
    data.append('file', input.files[0]);

    await fetch('/files/upload', {
        method: 'POST',
        body: data
    })

    loadFolder(currentPath);

}

function showCreateFolderDialog() {
    textInputDialog.style.display = 'flex';
}

function closeDialog() {
    textInputDialog.style.display = 'none';
}

function createFolder() {
    fetch('/folder/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(
            { 
                'path': currentPath,
                'name': textInput.value
            }
        )
    });

    loadFolder(currentPath);

    closeDialog();

    textInput.value = '';
}

function deleteFolder(folderName) {
    fetch('/folder/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(
            { 
                'path': currentPath,
                'name': folderName
            }
        )
    });

    loadFolder(currentPath);
}

function deleteFile(folderName) {
    fetch('/files/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(
            { 
                'path': currentPath,
                'name': folderName
            }
        )
    });

    loadFolder(currentPath);
}

loadFolder('');