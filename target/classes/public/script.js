document.addEventListener('DOMContentLoaded', () => {
    const fetchButton = document.getElementById('fetchApodButton');
    const apodContainer = document.getElementById('apodContainer');

    fetchButton.addEventListener('click', async () => {
        apodContainer.innerHTML = '<p class="loading-message">Fetching picture...</p>'; // Show loading message
        try {
            const response = await fetch('/apod'); // Calls our Java backend
            if (!response.ok) {
                let errorMsg = `Error: ${response.status}`;
                try {
                    const errorData = await response.json();
                    errorMsg += ` - ${errorData.error || 'Failed to fetch APOD data.'}`;
                } catch (e) {
                    // If parsing error response fails
                    errorMsg += ' - Could not parse error response.';
                }
                throw new Error(errorMsg);
            }

            const data = await response.json();
            displayApod(data);

        } catch (error) {
            console.error('Failed to fetch APOD:', error);
            apodContainer.innerHTML = `<p class="error-message">${error.message}</p>`;
        }
    });

    function displayApod(data) {
        apodContainer.innerHTML = ''; // Clear previous content or loading message

        if (data.title) {
            const titleElement = document.createElement('h2');
            titleElement.textContent = data.title;
            apodContainer.appendChild(titleElement);
        }

        if (data.media_type === 'image') {
            const imgElement = document.createElement('img');
            // Prefer HD URL if available, otherwise use standard URL
            imgElement.src = data.hdurl || data.url;
            imgElement.alt = data.title || 'NASA Astronomy Picture of the Day';
            apodContainer.appendChild(imgElement);
        } else if (data.media_type === 'video') {
            // Handle video - often YouTube links
            const videoLinkElement = document.createElement('p');
            const videoAnchor = document.createElement('a');
            videoAnchor.href = data.url;
            videoAnchor.textContent = `Today's APOD is a video. Click here to watch: ${data.url}`;
            videoAnchor.target = '_blank'; // Open in new tab
            videoLinkElement.appendChild(videoAnchor);
            apodContainer.appendChild(videoLinkElement);
        } else if (data.url) {
            // Fallback for other media types or if image URL is missing but a general URL exists
             const linkElement = document.createElement('p');
             linkElement.innerHTML = `The content type is '${data.media_type}'. View it here: <a href="${data.url}" target="_blank">${data.url}</a>`;
             apodContainer.appendChild(linkElement);
        }


        if (data.explanation) {
            const explanationElement = document.createElement('p');
            explanationElement.textContent = data.explanation;
            apodContainer.appendChild(explanationElement);
        }

        if (data.copyright) {
            const copyrightElement = document.createElement('p');
            copyrightElement.classList.add('copyright');
            copyrightElement.textContent = `Copyright: ${data.copyright}`;
            apodContainer.appendChild(copyrightElement);
        }
         if (!data.url && !data.hdurl) {
            apodContainer.innerHTML = '<p class="error-message">No image or video URL found in the data.</p>';
        }
    }
});
