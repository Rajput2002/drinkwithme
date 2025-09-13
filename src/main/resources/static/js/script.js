$(function() {

    // User Register validation
    var $userRegister = $("#userRegister");
    $userRegister.validate({
        rules: {
            name: {
                required: true,
                lettersonly: true
            },
            email: {
                required: true,
                space: true,
                email: true
            },
            mobileNumber: {
                required: true,
                space: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12
            },
            password: {
                required: true,
                space: true
            },
            confirmpassword: {
                required: true,
                space: true,
                equalTo: '#pass'
            },
            address: {
                required: true,
                all: true
            },
            city: {
                required: true,
                space: true
            },
            state: {
                required: true
            },
            pincode: {
                required: true,
                space: true,
                numericOnly: true
            },
            img: {
                required: true
            }
        },
        messages: {
            name: {
                required: 'Please enter your full name.',
                lettersonly: 'Names can only contain letters.'
            },
            email: {
                required: 'Please enter your email address.',
                space: 'Email should not contain spaces.',
                email: 'Please enter a valid email address.'
            },
            mobileNumber: {
                required: 'Please enter your mobile number.',
                space: 'Mobile number should not contain spaces.',
                numericOnly: 'Mobile number should only contain numbers.',
                minlength: 'Mobile number should be at least 10 digits long.',
                maxlength: 'Mobile number should not exceed 12 digits.'
            },
            password: {
                required: 'Please enter a password.',
                space: 'Password should not contain spaces.'
            },
            confirmpassword: {
                required: 'Please confirm your password.',
                space: 'Confirm password should not contain spaces.',
                equalTo: 'Your passwords do not match.'
            },
            address: {
                required: 'Please enter your address.',
                all: 'Address can contain alphanumeric characters and special characters (,.-).'
            },
            city: {
                required: 'Please enter your city.',
                space: 'City name should not contain spaces.'
            },
            state: {
                required: 'Please enter your state.'
            },
            pincode: {
                required: 'Please enter your pincode.',
                space: 'Pincode should not contain spaces.',
                numericOnly: 'Pincode should only contain numbers.'
            },
            img: {
                required: 'Please upload a profile image.'
            }
        }
    });

    // Orders Validation
    var $orders = $("#orders");
    $orders.validate({
        rules: {
            firstName: {
                required: true,
                lettersonly: true
            },
            lastName: {
                required: true,
                lettersonly: true
            },
            email: {
                required: true,
                space: true,
                email: true
            },
            mobileNo: {
                required: true,
                space: true,
                numericOnly: true,
                minlength: 10,
                maxlength: 12
            },
            address: {
                required: true,
                all: true
            },
            city: {
                required: true,
                space: true
            },
            state: {
                required: true
            },
            pincode: {
                required: true,
                space: true,
                numericOnly: true
            },
            paymentType: {
                required: true
            }
        },
        messages: {
            firstName: {
                required: 'Please enter your first name.',
                lettersonly: 'First name can only contain letters.'
            },
            lastName: {
                required: 'Please enter your last name.',
                lettersonly: 'Last name can only contain letters.'
            },
            email: {
                required: 'Please enter your email address.',
                space: 'Email should not contain spaces.',
                email: 'Please enter a valid email address.'
            },
            mobileNo: {
                required: 'Please enter your mobile number.',
                space: 'Mobile number should not contain spaces.',
                numericOnly: 'Mobile number should only contain numbers.',
                minlength: 'Mobile number should be at least 10 digits long.',
                maxlength: 'Mobile number should not exceed 12 digits.'
            },
            address: {
                required: 'Please enter your address.',
                all: 'Address can contain alphanumeric characters and special characters (,.-).'
            },
            city: {
                required: 'Please enter your city.',
                space: 'City name should not contain spaces.'
            },
            state: {
                required: 'Please enter your state.'
            },
            pincode: {
                required: 'Please enter your pincode.',
                space: 'Pincode should not contain spaces.',
                numericOnly: 'Pincode should only contain numbers.'
            },
            paymentType: {
                required: 'Please select a payment type.'
            }
        }
    });

    // Reset Password Validation
    var $resetPassword = $("#resetPassword");
    $resetPassword.validate({
        rules: {
            password: {
                required: true,
                space: true
            },
            confirmPassword: {
                required: true,
                space: true,
                equalTo: '#pass'
            }
        },
        messages: {
            password: {
                required: 'Please enter a new password.',
                space: 'New password should not contain spaces.'
            },
            confirmPassword: {
                required: 'Please confirm your new password.',
                space: 'Confirm password should not contain spaces.',
                equalTo: 'Your passwords do not match.'
            }
        }
    });

    // Custom Validator Methods
    jQuery.validator.addMethod('lettersonly', function(value, element) {
        return /^[^-\s][a-zA-Z_\s-]+$/.test(value);
    });

    jQuery.validator.addMethod('space', function(value, element) {
        return /^[^\s]+$/.test(value);
    });

    jQuery.validator.addMethod('all', function(value, element) {
        return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);
    });

    jQuery.validator.addMethod('numericOnly', function(value, element) {
        return /^[0-9]+$/.test(value);
    });
});

// Product Form Validation
function validateForm(event) {
    event.preventDefault();
    let title = document.forms["productForm"]["title"].value;
    let description = document.forms["productForm"]["description"].value;
    let category = document.forms["productForm"]["category"].value;
    let price = document.forms["productForm"]["price"].value;
    let stock = document.forms["productForm"]["stock"].value;
    let file = document.forms["productForm"]["file"].value;
    
    if (title.trim() === "" || description.trim() === "" || category === "--select--" || price === "" || stock === "" || file === "") {
        alert("All fields must be filled out");
        return false;
    }
    
    if (parseFloat(price) < 0) {
        alert("Price cannot be negative");
        return false;
    }
    
    if (parseInt(stock) < 0) {
        alert("Stock cannot be negative");
        return false;
    }
    
    let confirmation = confirm("Are you sure you want to submit the product?");
    if (confirmation) {
        document.getElementById("productForm").submit();
    }
}