const detailsButton = document.querySelector("#details-button");
const projectDetails = document.querySelector("#project-details");

detailsButton?.addEventListener("click", () => {
  const isExpanded = detailsButton.getAttribute("aria-expanded") === "true";
  detailsButton.setAttribute("aria-expanded", String(!isExpanded));
  projectDetails.hidden = isExpanded;
});
