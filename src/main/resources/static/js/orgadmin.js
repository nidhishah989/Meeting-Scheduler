
  const emailInput = document.querySelector('#adminEmail');
  const emailValidationMessage = document.querySelector('#emailValidationMessage');

  emailInput.addEventListener('input', function () {
    const email = emailInput.value;
    const emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

    if (emailPattern.test(email)) {
      emailValidationMessage.textContent = ''; // Valid email format
    } else {
      emailValidationMessage.textContent = 'Required email format- ref example: username@domain.com';
    }
  });

const passwordInput1 = document.getElementById('password1');
// Function to validate password pattern
passwordInput1.addEventListener('input', function () {
    const password = passwordInput1.value;
    const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}$/;

    if (passwordPattern.test(password)) {
        passwordValidationMessage.textContent = ''; // Valid password format
    } else {
        passwordValidationMessage.textContent = 'Password should contain at least 8 characters with a mix of letters, numbers, and special symbols.';
    }
});

const submitButton = document.getElementById('sign_up_btn');

submitButton.addEventListener('click', function(e) {
    const passwordInput = document.getElementById('password1').value;
    const confirmPasswordInput = document.getElementById('password2').value;
    const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}$/;

    if (!passwordPattern.test(passwordInput)) {
        alert('Password is not valid. It should contain at least 8 characters with a mix of letters, numbers, and special symbols.');
        e.preventDefault();
        return false;
    }

    if (passwordInput !== confirmPasswordInput) {
        alert('Password and Confirm Password do not match.');
        e.preventDefault();
        return false;
    }

    console.log("Event submitted");
});




