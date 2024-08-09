/*const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');*/
const EnterpriseButton = document.getElementById('Enterprise signIn');
const IndividualButton = document.getElementById('Individual signIn');
const container = document.getElementById('container');

if (EnterpriseButton && IndividualButton && container) {
        EnterpriseButton.addEventListener('click', () => {
            container.classList.remove("right-panel-active");
        });

        IndividualButton.addEventListener('click', () => {
            container.classList.add("right-panel-active");
        });
    } else {
        console.error('One or more elements not found:',
            { EnterpriseButton, IndividualButton, container });
    }