
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


/*Popup for games*/
// Open the popup
// Open the popup
function openPopup() {
    console.log("Opening")
    document.getElementById('popup').style.display = 'block';
    // document.addEventListener('click', closePopupOutside);
}

// Close the popup
function closePopup() {
    document.getElementById('popup').style.display = 'none';
    // document.removeEventListener('click', closePopupOutside);
}

// Close the popup if clicked outside
function closePopupOutside(event) {
    const popup = document.getElementById('popup');
    if (!popup.contains(event.target)) {
        closePopup();
    }
}


// Create a new game
function createGame(event) {
    event.preventDefault();

    // Get the form values
    const fullName = document.getElementById('fullName').value;
    const releaseDate = document.getElementById('releaseDate').value;
    const iconUrl = document.getElementById('iconUrl').value;
    const productionCompany = document.getElementById('productionCompany').value;

    // Here, you can send an AJAX request to your Spring Boot backend to save the new game details
    // For simplicity, I'll just log the data in the console.
    console.log('New game created:', fullName, releaseDate, iconUrl, productionCompany);

    // Close the popup
    closePopup();
}

/* popup for creating users*/
$(document).ready(function(){
    var createOrUpdate; // This will hold the action to be performed

    // Show the modal for creating a user
    $('#createButton').click(function(){
        createOrUpdate = 'create';
        $('#userModalLabel').text('New User');
        $('#userForm')[0].reset();
        $('#userModal').modal('show');
    });

    // Show the modal for updating a user
    $('.updateButton').click(function(){
        createOrUpdate = 'update';
        $('#userModalLabel').text('Update User');
        var id = $(this).data('id'); // Assuming that the id of the user is stored in 'data-id' attribute of the update button
        // Make an AJAX request to fetch the user details
        $.get('/admin/users/' + id, function(user){
            $('#userId').val(user.id);
            $('#name').val(user.name);
            $('#email').val(user.email);
            // Password should not be filled due to security reasons
        });
        $('#userModal').modal('show');
    });

    // Handle the form submission
    $('#saveButton').click(function(){
        var url, data;
        if (createOrUpdate === 'create') {
            url = '/admin/users/create';
            data = {
                name: $('#name').val(),
                email: $('#email').val(),
                password: $('#password').val(),
            };
        } else if (createOrUpdate === 'update') {
            url = '/admin/users/update/' + $('#userId').val();
            data = {
                id: $('#userId').val(),
                name: $('#name').val(),
                email: $('#email').val(),
                password: $('#password').val(),
            };
        }

        // Send the data to the server
        $.post(url, data, function(responseHtml){
            $('#userModal').modal('hide');
            // Refresh the page or update the users list
            location.reload();
        });
    });
});
