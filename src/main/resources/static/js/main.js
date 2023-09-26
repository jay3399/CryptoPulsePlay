document.addEventListener("DOMContentLoaded", () => {
  const emailInput = document.getElementById("emailInput");
  const submitButton = document.getElementById("submitButton");
  const messageElement = document.getElementById("message");
  const verifyButton = document.getElementById("verifyButton");

  let jwtToken = "";

  submitButton.addEventListener("click", async () => {
    const email = emailInput.value;
    const deviceInfo = "device";

    try {
      const response = await fetch("/signIn", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "DeviceInfo": deviceInfo
        },
        body: JSON.stringify({ email })
      });

      const data = await response.json();

      if (data.message) {
        messageElement.textContent = data.message;
      } else {
        jwtToken = data.loginToken;
        localStorage.setItem('loginToken', jwtToken);
        verifyButton.style.display = "block";
      }
    } catch (error) {
      console.error("Error during sign in or sign up:", error);
    }
  });

  verifyButton.addEventListener("click", async () => {

    const storedToken = localStorage.getItem("loginToken");

    if(!storedToken){
      alert("이메일 인증을 완료해주세요");
      return;
    }

    try {
      const response = await fetch(`/verifyEmail?token=${storedToken}`, {
        method: "GET"
      });

      if (response.ok) {
        window.location.href = "/mainPage";
      } else {
        alert("이메일 인증을 완료해주세요");
      }
    } catch (error) {
      console.error("Error during email verification:", error);
    }
  });
});
