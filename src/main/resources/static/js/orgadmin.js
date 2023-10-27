
const form = document.querySelector('form');
  const passwordInput = document.querySelector('#password1');
  const confirmPasswordInput = document.querySelector('#password2');
  const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}$/;

 const OpenAccount=(e)=>{
  e.preventDefault();
  const password = passwordInput.value;
  const confirmPassword = confirmPasswordInput.value;

  if (!passwordPattern.test(password)) {
    e.preventDefault();
    alert('Password is not valid. It should contain at least 8 characters with a mix of letters, numbers, and special symbols.');
  }

  if (password !== confirmPassword) {
    e.preventDefault();
    alert('Password and Confirm Password do not match.');
  }

  if (passwordPattern.test(password) && password === confirmPassword) {
      console.log("Form is valid");
      // Set the URL of the new page you want to navigate to
      // const newPageUrl = 'organizationsetup.html';
      // window.location.href = newPageUrl;
      const formData = {
              OrgName:{
                  orgName: document.querySelector('#organizationName').value,
                  teammates :{
                      admin:{
                          adminUsername: document.querySelector('#adminEmail').value.split('@')[0],
                          adminEmail: document.querySelector('#adminEmail').value,
                          adminpassword: document.querySelector('#adminEmail').value,
                          adminFirstname: document.querySelector('#firstName').value,
                          adminLastname: document.querySelector('#lastName').value,
                      }
                  }
              }
          };

      console.log(data);
      console.log(formData['OrgName']);
      data ['OrgName']=formData.OrgName;
      console.log(data);
      localStorage.setItem('data', JSON.stringify(data));
      window.location.href = 'organizationsetup.html';
    }
 }
  // form.addEventListener('submit', function (e) {
  //   const password = passwordInput.value;
  //   const confirmPassword = confirmPasswordInput.value;

  //   if (!passwordPattern.test(password)) {
  //     e.preventDefault();
  //     alert('Password is not valid. It should contain at least 8 characters with a mix of letters, numbers, and special symbols.');
  //   }

  //   if (password !== confirmPassword) {
  //     e.preventDefault();
  //     alert('Password and Confirm Password do not match.');
  //   }

  //   if (passwordPattern.test(password) && password === confirmPassword) {
  //       console.log("Form is valid");
  //       // Set the URL of the new page you want to navigate to
  //       // const newPageUrl = 'organizationsetup.html';
  //       // window.location.href = newPageUrl;
  //       const formData = {
  //               OrgName:{
  //                   orgName: document.querySelector('#organizationName').value,
  //                   teammates :{
  //                       admin:{
  //                           adminUsername: document.querySelector('#adminEmail').value.split('@')[0],
  //                           adminEmail: document.querySelector('#adminEmail').value,
  //                           adminpassword: document.querySelector('#adminEmail').value,
  //                           adminFirstname: document.querySelector('#firstName').value,
  //                           adminLastname: document.querySelector('#lastName').value,
  //                       }
  //                   }
  //               }
  //           };

  //       console.log(data);
  //       console.log(formData['OrgName']);
  //       data ['OrgName']=formData.OrgName;
  //       console.log(data);
  //       localStorage.setItem('data', JSON.stringify(data));


  //     }
  // });


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

