$(document).ready(function() {
    let trackList = $("#trackList");

    trackList.on("click", ".linkRemoveTrack", function(e) {
        e.preventDefault();
        deleteTrack($(this));
        updateTrackCountNumbers();
    });

    $("#track").on("click", "#linkAddTrack", function(e) {
        e.preventDefault();
        addNewTrackRecord();
    });

    trackList.on("change", ".dropDownStatus", function() {
        let dropDownList = $(this);
        let rowNumber = dropDownList.attr("rowNumber");
        let selectedOption = $("option:selected", dropDownList);

        let defaultNote = selectedOption.attr("defaultDescription");
        $("#trackNote" + rowNumber).text(defaultNote);
    });
});

function deleteTrack(link) {
    let rowNumber = link.attr('rowNumber');
    $("#rowTrack" + rowNumber).remove();
    $("#emptyLine" + rowNumber).remove();
}

function updateTrackCountNumbers() {
    $(".divCountTrack").each(function (index, element) {
        element.innerHTML = "" + (index + 1);
    });
}

function addNewTrackRecord() {
    let htmlCode = generateTrackCode();
    $("#trackList").append(htmlCode);
}

function generateTrackCode() {
    let nextCount = $(".hiddenTrackId").length + 1;
    let rowId = "rowTrack" + nextCount;
    let emptyLineId = "emptyLine" + nextCount;
    let trackNoteId = "trackNote" + nextCount;
    let currentDateTime = formatCurrentDateTime();

    let htmlCode = `
			<div class="row border rounded p-1" id="${rowId}">
				<input type="hidden" name="trackId" value="0" class="hiddenTrackId" />
				<div class="col-2">
					<div class="divCountTrack">${nextCount}</div>
					<div class="mt-1"><a class="fas fa-trash icon-dark linkRemoveTrack" href="" rowNumber="${nextCount}"></a></div>					
				</div>				
				
				<div class="col-10">
				  <div class="form-group row">
				    <label class="col-form-label">Time:</label>
				    <div class="col">
						<input type="datetime-local" name="trackDate" value="${currentDateTime}" class="form-control" required
							style="max-width: 300px"/>						
				    </div>
				  </div>					
				<div class="form-group row">  
				<label class="col-form-label">Status:</label>
				<div class="col">
					<select name="trackStatus" class="form-control dropDownStatus" required style="max-width: 150px" rowNumber="${nextCount}">
			`;

    htmlCode += $("#trackStatusOptions").clone().html();

    htmlCode += `
				      </select>						
				    </div>
				  </div>
				  <div class="form-group row">
				    <label class="col-form-label">Notes:</label>
				    <div class="col">
						<textarea rows="2" cols="10" class="form-control" name="trackNotes" id="${trackNoteId}" style="max-width: 300px" required></textarea>
				    </div>
				  </div>
				  
				</div>				
			</div>	
			<div id="${emptyLineId}" class="row">&nbsp;</div>
	`;

    return htmlCode;
}

function formatCurrentDateTime() {
    date = new Date();
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let hour = date.getHours();
    let minute = date.getMinutes();
    let second = date.getSeconds();

    if (month < 10) month = "0" + month;
    if (day < 10) day = "0" + day;

    if (hour < 10) hour = "0" + hour;
    if (minute < 10) minute = "0" + minute;
    if (second < 10) second = "0" + second;

    return year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second;

}