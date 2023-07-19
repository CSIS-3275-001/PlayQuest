
$(document).ready(function() {
    // Listen for file input changes
    $('#photos').on('change', function() {
        var files = $(this)[0].files;
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
    });
});