//     //adjust team-member or client form
//     $(document).ready(function() {
    $('button[data-bs-target="#addMemberModal"]').click(function() {
        console.log("Admin JS")
        var role = $(this).data('role');
        var action = $(this).data('action');
        console.log("Role: ",role)
        console.log("Action: ",action)

        // Update the hidden input fields in the form
        $('#roleInput').val(role);
        $('#actionInput').val(action);
        $('#addMemberModalLabel').text(role === 'teammember'? 'Add TeamMember': 'Add Client');
    });

$('button[data-bs-target="#addClientModal"]').click(function() {
    console.log("Admin JS")
    var rolecl = $(this).data('role');
    var actioncl = $(this).data('action');
    console.log("Role: ",rolecl)
    console.log("Action: ",actioncl)

    // Update the hidden input fields in the form
    $('#clientRole').val(rolecl);
    // $('#actionInput').val(action);
    $('#addClientModalLabel').text(rolecl === 'teammember'? 'Add TeamMember': 'Add Client');
});