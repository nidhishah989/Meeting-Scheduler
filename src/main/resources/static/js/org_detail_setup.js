
document.addEventListener("DOMContentLoaded", function () {
    const countryDropdown = document.getElementById("countryDropdown");
    const stateDropdown = document.getElementById("stateDropdown");
    const orgname = document.querySelector('#orgname');

    const organizationData = JSON.parse(localStorage.getItem('data'));
    const organizationname = organizationData['OrgName']['orgName'];
    console.log(organizationData);
    console.log(organizationname);

    // get org name
    orgname.value= organizationname;
    // Load the JSON data
    fetch("states.json")  // Assuming states.json is in the same directory as your HTML file
        .then(response => response.json())
        .then(stateData => {
            countryDropdown.addEventListener("change", function () {
                const selectedCountry = countryDropdown.value;
                stateDropdown.innerHTML = ''; // Clear the state dropdown

                if (selectedCountry === "USA") {
                    stateData[selectedCountry].forEach(function (state) {
                        console.log(state);
                        const option = document.createElement("option");
                        option.value = state;
                        option.textContent = state;
                        stateDropdown.appendChild(option);
                    });
                }
            });
        })
        .catch(error => {
            console.error("Failed to load states data:", error);
        });


});

const city_input = document.querySelector("#city");
const cityvaluemessage = document.querySelector('#cityvaluemessage');
city_input.addEventListener("input",function(){
    const value = city_input.value;
    const city_pattern = /[A-Za-z]+$/;
    console.log(city_input.value);
    if(city_pattern.test(value)){
        cityvaluemessage.textContent ='';
    }
    else{
        console.log("$$")
        cityvaluemessage.textContent="Invalid city name.";
    }
});

const saveOrgData = (e) => {
    e.preventDefault();
    const organizationData = JSON.parse(localStorage.getItem('data'));
    const orgData = {
        organizationDescr: document.querySelector("#organizationdetail").value,
        orgcontactNo: document.querySelector("#phoneNumber").value,
        orgAddress: document.querySelector("#address1").value + document.querySelector("#address2").value + document.querySelector("#city").value,
        orgState: document.querySelector("#stateDropdown").value,
        orgCountry: document.querySelector("#countryDropdown").value,
    };

    // console.log("Old Data: ", organizationData);

    // Check if 'OrgName' key exists in organizationData
    if (!organizationData['OrgName']) {
        organizationData['OrgName'] = {}; // Create an empty object if 'OrgName' doesn't exist
    }

    // Merge the old data in organizationData['OrgName'] with orgData
    organizationData['OrgName'] = { ...organizationData['OrgName'], ...orgData };

    // console.log("Updated Data: ", organizationData);

    // Store the updated data back in localStorage
    localStorage.setItem('data', JSON.stringify(organizationData));

    // console.log("New Data: ", JSON.parse(localStorage.getItem('data')));

    window.location.href = 'AdminPage.html';

};

