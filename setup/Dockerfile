# Base image
FROM jenkins/jenkins:2.492.3-lts-jdk21

# Switch to root user for installations
USER root

# Update and install prerequisites
# Added lsb-release which is needed for $(lsb_release -cs)
RUN apt-get update && apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    software-properties-common

# Add Docker's official GPG key
RUN install -m 0755 -d /etc/apt/keyrings
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
RUN chmod a+r /etc/apt/keyrings/docker.gpg

# Set up the Docker stable repository
RUN echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

# Update apt package index again and install Docker CLI
RUN apt-get update && apt-get install -y docker-ce-cli

# Install docker-compose (keeping your existing method)
RUN curl -L "https://github.com/docker/compose/releases/download/$(curl -fsSLI -o /dev/null -w %{url_effective} https://github.com/docker/compose/releases/latest | sed 's#.*tag/##g')/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose && \
    ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

# Switch back to jenkins user
USER jenkins

# Install Jenkins plugins
RUN jenkins-plugin-cli --plugins blueocean
RUN jenkins-plugin-cli --plugins json-path-api:2.9.0-58.v62e3e85b_a_655
