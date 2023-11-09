document.addEventListener("DOMContentLoaded", function () {
    // Get the timezone select element
    const timezoneSelect = document.getElementById("timezone");

    // Get the list of timezones and populate the dropdown
    moment.tz.names().forEach(function (timezone) {
        const option = document.createElement("option");
        option.value = timezone;
        option.text = timezone + ' - ' + moment.tz(timezone).format('z (Z)');
        timezoneSelect.appendChild(option);
    });

    // Detect the user's browser timezone and set it as the default selected option
    const browserTimezone = moment.tz.guess();
    timezoneSelect.value = browserTimezone;

    // Get the Zoom checkbox and Zoom Link input elements
    const zoomCheckbox = document.getElementById("zoom");
    const zoomLinkDiv = document.getElementById("zoomLinkDiv");

    // Add an event listener to the Zoom checkbox
    zoomCheckbox.addEventListener("change", function () {
        if (zoomCheckbox.checked) {
            // If the Zoom checkbox is checked, show the Zoom Link input
            zoomLinkDiv.style.display = "block";
        } else {
            // If the Zoom checkbox is unchecked, hide the Zoom Link input
            zoomLinkDiv.style.display = "none";
        }
    });
});
  