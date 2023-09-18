# Use the Jenkins base image with JDK 11
FROM jenkins/jenkins:2.375-jdk11

# Set the SHELL option with -o pipefail for proper error handling
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Switch to the root user to perform system-level operations
USER root

# Update package lists and install necessary packages
RUN apt-get update && apt-get install --no-install-recommends -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg2 \
    software-properties-common \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Add Docker's official GPG key to the system
RUN install -m 0755 -d /etc/apt/keyrings
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
RUN chmod a+r /etc/apt/keyrings/docker.gpg

# Add the Docker repository to Apt sources
RUN echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

# Update package lists and install Docker CLI
RUN apt-get update && apt-get install --no-install-recommends -y \
    docker-ce-cli \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Download and install Docker Compose
RUN curl -L "https://github.com/docker/compose/releases/download/$(curl -fsSLI -o /dev/null -w %'{'url_effective'}' https://github.com/docker/compose/releases/latest | sed 's#.*tag/##g' && echo)/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose \
    && chmod +x /usr/local/bin/docker-compose \
    && ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

# Switch back to the Jenkins user for Jenkins-specific setup
USER jenkins

# Install the Blue Ocean Jenkins plugin using the Jenkins Plugin CLI
RUN ["jenkins-plugin-cli", "--plugins", "blueocean"]
