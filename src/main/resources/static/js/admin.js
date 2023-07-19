
function previewFiles(files) {
    $('#previewImages').empty();

    for (var i = 0; i < files.length; i++) {
        var file = files[i];
        var reader = new FileReader();

        reader.onload = function(e) {
            var imageUrl = e.target.result;

            var imageElement = $('<div class="col-md-3">' +
                '<img src="' + imageUrl + '" class="img-thumbnail">' +
                '</div>');

            $('#previewImages').append(imageElement);
        };

        reader.readAsDataURL(file);
    }
}

function validateFileSelection(input, maxFiles) {
    var files = input.files;
    var allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;

    if (files.length > maxFiles) {
        alert('You can only select up to ' + maxFiles + ' files.');
        input.value = '';
        return;
    }

    for (var i = 0; i < files.length; i++) {
        var file = files[i];

        if (!allowedExtensions.exec(file.name)) {
            alert('Please select image files only (JPG, JPEG, PNG, GIF).');
            input.value = '';
            return;
        }
    }

    previewFiles(files);
}