function htmlToFragment(html) {
    const template = document.createElement("template");
    template.innerHTML = html;
    return template.content;
}
// default size: width = 16
function createCheckedIcon(width = 16) {
    // In courtesy of my guru ChatGPT*

    const svgNS = "http://www.w3.org/2000/svg";

    // Create <svg>
    const checkedEl = document.createElementNS(svgNS, 'svg');
    checkedEl.setAttribute('width', String(width));
    checkedEl.setAttribute('viewBox', '0 0 16 16');
    checkedEl.setAttribute('fill', 'currentColor');
    checkedEl.setAttribute('class', 'bi bi-check-circle-fill');

    // Create <path>
    const path = document.createElementNS(svgNS, "path");
    path.setAttribute('d', 'M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z');

    // Append path to svg
    checkedEl.appendChild(path);

    // Append svg to body (or wherever you want)
    return checkedEl;
}
// default size: width = 16
function createUncheckedIcon(width = 16) {
    // In courtesy of my guru ChatGPT

    const svgNS = "http://www.w3.org/2000/svg";

    // Create <svg>
    const uncheckedEl = document.createElementNS(svgNS, 'svg');
    uncheckedEl.setAttribute('width', String(width));
    uncheckedEl.setAttribute('viewBox', '0 0 16 16');
    uncheckedEl.setAttribute('fill', 'currentColor');
    uncheckedEl.setAttribute('class', 'bi bi-circle');

    // Create <path>
    const path = document.createElementNS(svgNS, 'path');
    path.setAttribute('d', 'M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16');

    // Append path to svg
    uncheckedEl.appendChild(path);

    // Append svg to body (or wherever you want)
    return uncheckedEl;
}