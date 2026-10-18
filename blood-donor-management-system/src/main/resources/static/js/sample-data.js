(() => {
  window.BLOODBANK_SAMPLE_DATA = Object.freeze({
    donors: [
      { name: "Asha Sharma", area: "North District", group: "O+", lastDonation: "18 Sep 2026", eligibility: "18 Dec 2026", status: "Cooling period" },
      { name: "Kabir Mehta", area: "Riverside", group: "A−", lastDonation: "12 Jun 2026", eligibility: "Eligible now", status: "Eligible" },
      { name: "Mira Nair", area: "Central District", group: "B+", lastDonation: "03 Aug 2026", eligibility: "Eligible now", status: "Available" },
      { name: "Dev Patel", area: "Eastside", group: "AB+", lastDonation: "25 May 2026", eligibility: "Eligible now", status: "Eligible" },
      { name: "Tara Iyer", area: "Lakeside", group: "O−", lastDonation: "29 Sep 2026", eligibility: "29 Dec 2026", status: "Cooling period" },
      { name: "Neil Das", area: "West End", group: "B−", lastDonation: "10 Jul 2026", eligibility: "Eligible now", status: "Available" },
      { name: "Leena Roy", area: "Old Town", group: "A+", lastDonation: "16 Apr 2026", eligibility: "Eligible now", status: "Eligible" },
      { name: "Arjun Sen", area: "South District", group: "AB−", lastDonation: "02 Oct 2026", eligibility: "02 Jan 2027", status: "Cooling period" },
    ],
    recipients: [
      { name: "Patient case R-204", reference: "Case R-204", facility: "Cedar Medical Centre", group: "O−", units: 2, priority: "Urgent", status: "Pending" },
      { name: "Patient case R-198", reference: "Case R-198", facility: "Riverside Hospital", group: "A+", units: 3, priority: "High", status: "Under review" },
      { name: "Patient case R-191", reference: "Case R-191", facility: "Northview Clinic", group: "B+", units: 1, priority: "Normal", status: "Approved" },
      { name: "Patient case R-185", reference: "Case R-185", facility: "Greenfield Hospital", group: "AB+", units: 2, priority: "High", status: "Pending" },
      { name: "Patient case R-178", reference: "Case R-178", facility: "Cedar Medical Centre", group: "A−", units: 1, priority: "Normal", status: "Fulfilled" },
    ],
    donations: [
      { donor: "Mira Nair", reference: "DN-1048", group: "B+", date: "18 Oct 2026", center: "Central Collection Centre", units: 1, status: "Completed" },
      { donor: "Kabir Mehta", reference: "DN-1047", group: "A−", date: "17 Oct 2026", center: "Riverside Donor Hub", units: 1, status: "Completed" },
      { donor: "Leena Roy", reference: "DN-1046", group: "A+", date: "16 Oct 2026", center: "Northview Collection Centre", units: 1, status: "Completed" },
      { donor: "Neil Das", reference: "DN-1045", group: "B−", date: "15 Oct 2026", center: "West End Donor Hub", units: 1, status: "Completed" },
      { donor: "Dev Patel", reference: "DN-1044", group: "AB+", date: "14 Oct 2026", center: "Central Collection Centre", units: 1, status: "Completed" },
      { donor: "Asha Sharma", reference: "DN-1043", group: "O+", date: "12 Oct 2026", center: "Lakeside Donor Hub", units: 1, status: "Completed" },
    ],
    requests: [
      { facility: "Cedar Medical Centre", reference: "RQ-3021", group: "O−", units: 2, submitted: "Today · 09:42", priority: "Urgent", status: "Pending" },
      { facility: "Riverside Hospital", reference: "RQ-3018", group: "A+", units: 4, submitted: "Today · 08:15", priority: "High", status: "Under review" },
      { facility: "Northview Clinic", reference: "RQ-3014", group: "B+", units: 2, submitted: "17 Oct 2026", priority: "Normal", status: "Approved" },
      { facility: "Greenfield Hospital", reference: "RQ-3009", group: "AB+", units: 1, submitted: "16 Oct 2026", priority: "High", status: "Pending" },
      { facility: "Cedar Medical Centre", reference: "RQ-3005", group: "A−", units: 3, submitted: "15 Oct 2026", priority: "Normal", status: "Fulfilled" },
    ],
    inventory: [
      { group: "A+", units: 42, location: "Cold room A · Shelf 01", updated: "18 Oct 2026", status: "In stock" },
      { group: "A−", units: 12, location: "Cold room A · Shelf 02", updated: "18 Oct 2026", status: "Low stock" },
      { group: "B+", units: 36, location: "Cold room B · Shelf 01", updated: "17 Oct 2026", status: "In stock" },
      { group: "B−", units: 9, location: "Cold room B · Shelf 02", updated: "17 Oct 2026", status: "Low stock" },
      { group: "O+", units: 58, location: "Cold room O · Shelf 01", updated: "18 Oct 2026", status: "In stock" },
      { group: "O−", units: 14, location: "Cold room O · Shelf 02", updated: "18 Oct 2026", status: "Low stock" },
      { group: "AB+", units: 23, location: "Cold room AB · Shelf 01", updated: "16 Oct 2026", status: "In stock" },
      { group: "AB−", units: 7, location: "Cold room AB · Shelf 02", updated: "16 Oct 2026", status: "Low stock" },
    ],
  });
})();
