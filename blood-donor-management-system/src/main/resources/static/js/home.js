(() => {
  const demoData = window.BLOODBANK_SAMPLE_DATA;

  const initials = (name) =>
    name.split(/\s+/).filter(Boolean).slice(0, 2).map((part) => part[0]).join("").toUpperCase();

  const textCell = (value, className = "") => {
    const cell = document.createElement("td");
    if (className) cell.className = className;
    cell.textContent = value ?? "—";
    return cell;
  };

  const personCell = (name, detail) => {
    const cell = document.createElement("td");
    const person = document.createElement("div");
    person.className = "table-person";
    const avatar = document.createElement("span");
    avatar.className = "person-avatar";
    avatar.setAttribute("aria-hidden", "true");
    avatar.textContent = initials(name);
    const copy = document.createElement("span");
    copy.className = "person-copy";
    const strong = document.createElement("strong");
    strong.textContent = name;
    const small = document.createElement("small");
    small.textContent = detail;
    copy.append(strong, small);
    person.append(avatar, copy);
    cell.append(person);
    return cell;
  };

  const groupCell = (group) => {
    const cell = document.createElement("td");
    const badge = document.createElement("span");
    badge.className = "blood-group-badge";
    badge.textContent = group;
    cell.append(badge);
    return cell;
  };

  const statusCell = (status) => {
    const cell = document.createElement("td");
    const badge = document.createElement("span");
    const statusClass = {
      "Available": "status-positive",
      "Eligible": "status-positive",
      "Completed": "status-positive",
      "In stock": "status-positive",
      "Low stock": "status-warning",
      "Cooling period": "status-neutral",
      "Under review": "status-warning",
      "Urgent": "status-danger",
      "High": "status-danger",
      "Normal": "status-neutral",
      "Pending": "status-warning",
      "Approved": "status-positive",
      "Fulfilled": "status-positive",
    }[status] || "status-neutral";
    badge.className = `status-pill ${statusClass}`;
    badge.textContent = status;
    cell.append(badge);
    return cell;
  };

  if (demoData) {
  const tableRows = {
    donors: {
      rows: demoData.donors,
      render: (row) => [
        personCell(row.name, row.area),
        groupCell(row.group),
        textCell(row.lastDonation),
        textCell(row.eligibility),
        statusCell(row.status),
      ],
    },
    recipients: {
      rows: demoData.recipients,
      render: (row) => [
        personCell(row.name, row.reference),
        textCell(row.facility),
        groupCell(row.group),
        textCell(`${row.units} units`),
        statusCell(row.priority),
        statusCell(row.status),
      ],
    },
    donations: {
      rows: demoData.donations,
      render: (row) => [
        personCell(row.donor, row.reference),
        groupCell(row.group),
        textCell(row.date),
        textCell(row.center),
        textCell(`${row.units} unit${row.units === 1 ? "" : "s"}`),
        statusCell(row.status),
      ],
    },
    requests: {
      rows: demoData.requests,
      render: (row) => [
        personCell(row.facility, row.reference),
        groupCell(row.group),
        textCell(`${row.units} units`),
        textCell(row.submitted),
        statusCell(row.priority),
        statusCell(row.status),
      ],
    },
    inventory: {
      rows: demoData.inventory,
      render: (row) => [
        groupCell(row.group),
        textCell(`${row.units} units`),
        textCell(row.location),
        textCell(row.updated),
        statusCell(row.status),
      ],
    },
  };

  document.querySelectorAll("[data-sample-table]").forEach((body) => {
    const config = tableRows[body.dataset.sampleTable];
    if (!config) return;
    const limit = Number(body.dataset.rowLimit) || config.rows.length;
    config.rows.slice(0, limit).forEach((record) => {
      const row = document.createElement("tr");
      config.render(record).forEach((cell) => row.append(cell));
      body.append(row);
    });
  });

  const stockGrid = document.querySelector("[data-stock-grid]");
  if (stockGrid) {
    demoData.inventory.forEach((stock) => {
      const card = document.createElement("article");
      card.className = "stock-tile";
      const group = document.createElement("span");
      group.className = "stock-group";
      group.textContent = stock.group;
      const units = document.createElement("strong");
      units.className = "stock-units";
      units.textContent = String(stock.units);
      const label = document.createElement("span");
      label.className = "stock-label";
      label.textContent = "units available";
      const status = document.createElement("span");
      status.className = `stock-state ${stock.status === "Low stock" ? "stock-low" : "stock-ok"}`;
      status.textContent = stock.status;
      card.append(group, units, label, status);
      stockGrid.append(card);
    });
  }

  const requestList = document.querySelector("[data-pending-requests]");
  if (requestList) {
    demoData.requests.filter((request) => request.status === "Pending" || request.priority === "Urgent")
      .slice(0, 4).forEach((request) => {
        const item = document.createElement("li");
        item.className = "request-item";
        const group = document.createElement("span");
        group.className = "request-group";
        group.textContent = request.group;
        const details = document.createElement("span");
        details.className = "request-details";
        const title = document.createElement("strong");
        title.textContent = request.facility;
        const meta = document.createElement("small");
        meta.textContent = `${request.units} units · ${request.submitted}`;
        details.append(title, meta);
        const priority = document.createElement("span");
        priority.className = `request-priority ${request.priority === "Urgent" ? "priority-urgent" : ""}`;
        priority.textContent = request.priority;
        item.append(group, details, priority);
        requestList.append(item);
      });
  }
  }

  document.querySelectorAll("[data-table-search]").forEach((input) => {
    const tableName = input.dataset.tableSearch;
    const table = document.querySelector(`[data-sample-table="${tableName}"]`);
    const emptyState = document.querySelector(`[data-empty-state="${tableName}"]`);
    if (!table) return;
    input.addEventListener("input", () => {
      const query = input.value.trim().toLocaleLowerCase();
      let visibleRows = 0;
      table.querySelectorAll("tr").forEach((row) => {
        const matches = row.textContent.toLocaleLowerCase().includes(query);
        row.hidden = !matches;
        if (matches) visibleRows += 1;
      });
      if (emptyState) emptyState.hidden = visibleRows > 0;
    });
  });

  document.querySelectorAll("[data-panel-toggle]").forEach((button) => {
    const panel = document.querySelector(button.dataset.panelToggle);
    if (!panel) return;
    button.addEventListener("click", () => {
      panel.hidden = !panel.hidden;
      button.setAttribute("aria-expanded", String(!panel.hidden));
      if (!panel.hidden) panel.querySelector("input, select, textarea")?.focus();
    });
  });

  document.querySelectorAll("[data-prototype-form]").forEach((form) => {
    const submitButton = form.querySelector('button[type="submit"]');
    if (submitButton) submitButton.disabled = false;
    form.addEventListener("submit", (event) => {
      event.preventDefault();
      const feedback = form.querySelector("[data-form-feedback]");
      if (feedback) {
        feedback.textContent = form.dataset.prototypeForm === "login"
          ? "Prototype only: authentication is not active. Nothing was sent or saved."
          : "Prototype only: this form is not connected to storage. Nothing was saved.";
        feedback.hidden = false;
      }
    });
  });

  const menuButton = document.querySelector("[data-menu-button]");
  const sidebar = document.querySelector("#sidebar");
  const overlay = document.querySelector("[data-sidebar-overlay]");
  const closeMenu = () => {
    sidebar?.classList.remove("is-open");
    document.body.classList.remove("menu-open");
    if (overlay) overlay.hidden = true;
    menuButton?.setAttribute("aria-expanded", "false");
  };
  menuButton?.addEventListener("click", () => {
    const open = menuButton.getAttribute("aria-expanded") !== "true";
    sidebar?.classList.toggle("is-open", open);
    document.body.classList.toggle("menu-open", open);
    if (overlay) overlay.hidden = !open;
    menuButton.setAttribute("aria-expanded", String(open));
  });
  overlay?.addEventListener("click", closeMenu);
  sidebar?.querySelectorAll("a").forEach((link) => link.addEventListener("click", closeMenu));
  document.addEventListener("keydown", (event) => {
    if (event.key === "Escape") closeMenu();
  });
})();
