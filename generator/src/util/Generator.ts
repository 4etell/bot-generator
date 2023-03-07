import JSZip from 'jszip';
import {v4 as uuidv4} from 'uuid';
import {FormValues} from "../components/GeneratorForm";

const createEnvFile = (values: FormValues): File => {
    // Исходная текстовая строка для сохранения
    const content = `RABBITMQ_USER=${uuidv4()}
RABBITMQ_PASSWORD=${uuidv4()}
TELEGRAM_BOT_TOKEN=${values.telegramToken}
MINIO_USER=${uuidv4()}
MINIO_PASSWORD=${uuidv4()}
COMPOSE_PROJECT_NAME=bot
MESSAGE_BROKER_REPLICAS=${values.messageBrokerReplicas}
MESSAGE_HANDLER_REPLICAS=${values.messageHandlerReplicas}
CONFIG_SERVICE_USERNAME=${values.adminUsername}
CONFIG_SERVICE_PASSWORD=${values.adminPassword}
EMAIL=${values.email}
APP_DOMAIN=${values.domain}`;

    return new File([content], ".env", {type: "text/plain"});
}

const createComposeFile = (): File => {
    const content = "version: '3.8'\n" +
        "services:\n" +
        "  reverse-proxy:\n" +
        "    image: \"jwilder/nginx-proxy:latest\"\n" +
        "    container_name: \"reverse-proxy\"\n" +
        "    volumes:\n" +
        "        - \"html:/usr/share/nginx/html\"\n" +
        "        - \"dhparam:/etc/nginx/dhparam\"\n" +
        "        - \"vhost:/etc/nginx/vhost.d\"\n" +
        "        - \"certs:/etc/nginx/certs\"\n" +
        "        - \"/run/docker.sock:/tmp/docker.sock:ro\"\n" +
        "    restart: \"always\"\n" +
        "    networks: \n" +
        "        - \"bot-net\"\n" +
        "    ports:\n" +
        "        - \"80:80\"\n" +
        "        - \"443:443\" \n" +
        "  letsencrypt:\n" +
        "    image: \"jrcs/letsencrypt-nginx-proxy-companion:latest\"\n" +
        "    container_name: \"letsencrypt-helper\"\n" +
        "    volumes:\n" +
        "        - \"html:/usr/share/nginx/html\"\n" +
        "        - \"dhparam:/etc/nginx/dhparam\"\n" +
        "        - \"vhost:/etc/nginx/vhost.d\"\n" +
        "        - \"certs:/etc/nginx/certs\"\n" +
        "        - \"/run/docker.sock:/var/run/docker.sock:ro\"\n" +
        "    environment:\n" +
        "        NGINX_PROXY_CONTAINER: \"reverse-proxy\"\n" +
        "        DEFAULT_EMAIL: ${EMAIL}\n" +
        "    restart: \"always\"\n" +
        "    depends_on:\n" +
        "        - \"reverse-proxy\"\n" +
        "    networks: \n" +
        "        - \"bot-net\"\n" +
        "       \n" +
        "  rabbitmq:\n" +
        "    image: rabbitmq:3.8.22-management-alpine\n" +
        "    container_name: rabbitmq\n" +
        "    restart: always\n" +
        "    networks:\n" +
        "        - rabbitmq-net\n" +
        "    healthcheck:\n" +
        "      test: rabbitmq-diagnostics -q ping\n" +
        "      interval: 30s\n" +
        "      timeout: 30s\n" +
        "      retries: 3\n" +
        "    environment:\n" +
        "      RABBITMQ_DEFAULT_USER: ${RABBITMQ_USER}\n" +
        "      RABBITMQ_DEFAULT_PASS: ${RABBITMQ_PASSWORD}\n" +
        "    volumes:\n" +
        "      - ./rabbitmq-data:/var/lib/rabbitmq\n" +
        "  \n" +
        "  message-broker:\n" +
        "    image: 4etell/message-broker:0.0.1\n" +
        "    deploy:\n" +
        "        replicas: ${MESSAGE_BROKER_REPLICAS}\n" +
        "    restart: always\n" +
        "    networks:\n" +
        "        - rabbitmq-net\n" +
        "        - bot-net\n" +
        "    environment:\n" +
        "        TELEGRAM_BOT_TOKEN: ${TELEGRAM_BOT_TOKEN}\n" +
        "        RABBITMQ_USER: ${RABBITMQ_USER}\n" +
        "        RABBITMQ_PASSWORD: ${RABBITMQ_PASSWORD}\n" +
        "        RABBITMQ_URI: amqp://rabbitmq:5672\n" +
        "        VIRTUAL_HOST: ${APP_DOMAIN}\n" +
        "        LETSENCRYPT_HOST: ${APP_DOMAIN}\n" +
        "        VIRTUAL_PATH: /message-broker\n" +
        "        VIRTUAL_PORT: 8083\n" +
        "    depends_on:\n" +
        "      rabbitmq:\n" +
        "        condition: service_healthy  \n" +
        "        \n" +
        "  message-handler:\n" +
        "    image: 4etell/message-handler:0.0.1\n" +
        "    restart: always\n" +
        "    deploy:\n" +
        "        replicas: ${MESSAGE_HANDLER_REPLICAS}\n" +
        "    networks:\n" +
        "        - rabbitmq-net\n" +
        "    environment:\n" +
        "        CONFIG_SERVICE_URL: http://config-service:8089/config-service\n" +
        "        RABBITMQ_USER: ${RABBITMQ_USER}\n" +
        "        RABBITMQ_PASSWORD: ${RABBITMQ_PASSWORD}\n" +
        "        CONFIG_SERVICE_USERNAME: ${CONFIG_SERVICE_USERNAME}\n" +
        "        CONFIG_SERVICE_PASSWORD: ${CONFIG_SERVICE_PASSWORD}\n" +
        "        RABBITMQ_URI: amqp://rabbitmq:5672\n" +
        "    depends_on:\n" +
        "      rabbitmq:\n" +
        "        condition: service_healthy   \n" +
        "        \n" +
        "  config-service:\n" +
        "    image: 4etell/config-service:0.0.1\n" +
        "    container_name: config-service\n" +
        "    restart: always\n" +
        "    networks:\n" +
        "        - rabbitmq-net\n" +
        "        - bot-net\n" +
        "        - minio-net\n" +
        "    environment:\n" +
        "        BASIC_AUTH_USERNAME: ${CONFIG_SERVICE_USERNAME}\n" +
        "        BASIC_AUTH_PASSWORD: ${CONFIG_SERVICE_PASSWORD}\n" +
        "        MINIO_ACCESS_KEY: ${MINIO_USER}\n" +
        "        MINIO_SECRET_KEY: ${MINIO_PASSWORD}\n" +
        "        MINIO_URI: http://minio:9000\n" +
        "        RABBITMQ_USER: ${RABBITMQ_USER}\n" +
        "        RABBITMQ_PASSWORD: ${RABBITMQ_PASSWORD}\n" +
        "        RABBITMQ_URI: amqp://rabbitmq:5672\n" +
        "        VIRTUAL_PATH: /config-service/admin\n" +
        "        VIRTUAL_HOST: ${APP_DOMAIN}\n" +
        "        LETSENCRYPT_HOST: ${APP_DOMAIN}\n" +
        "        VIRTUAL_PORT: 8089\n" +
        "    depends_on:\n" +
        "      minio:\n" +
        "        condition: service_healthy\n" +
        "      rabbitmq:\n" +
        "        condition: service_healthy\n" +
        "  minio:\n" +
        "    image: minio/minio\n" +
        "    container_name: minio\n" +
        "    restart: always\n" +
        "    networks:\n" +
        "        - minio-net\n" +
        "    healthcheck:\n" +
        "      test: [\"CMD\", \"curl\", \"-f\", \"http://localhost:9000/minio/health/live\"]\n" +
        "      interval: 30s\n" +
        "      timeout: 10s\n" +
        "      retries: 5\n" +
        "    environment:\n" +
        "      MINIO_ROOT_USER: ${MINIO_USER}\n" +
        "      MINIO_ROOT_PASSWORD: ${MINIO_PASSWORD}\n" +
        "      MINIO_BROWSER: \"on\"\n" +
        "    volumes:\n" +
        "      - ./minio-data:/data\n" +
        "      - ./minio-config:/root/.minio\n" +
        "    command: server /data --console-address \":9005\"\n" +
        "\n" +
        "networks:\n" +
        "    bot-net:\n" +
        "        external: true\n" +
        "    rabbitmq-net:\n" +
        "        driver: bridge\n" +
        "    minio-net:\n" +
        "        driver: bridge\n" +
        "\n" +
        "volumes:\n" +
        "  certs:\n" +
        "  html:\n" +
        "  vhost:\n" +
        "  dhparam:\n"
    return new File([content], "docker-compose.yaml", {type: "text/plain"});
}

const createBashScriptFile = (values: FormValues) => {
    const content = `# Install curl
if ! command -v curl &> /dev/null
then
    echo "Installing curl..."
    sudo apt-get update
    sudo apt-get install -y curl
    echo "curl installed."
else
    echo "curl already installed."
fi

# Create network
docker network create bot-net

# Start the containers defined in the docker-compose.yml file
docker-compose up -d

# Setup telegram webhook
curl -F "url=https://${values.domain}/message-broker/telegram" https://api.telegram.org/bot${values.telegramToken}/setWebhook

echo " Telegram webhook done! "
sleep 30
echo "All installed. You can change bot config in admin panel: https://${values.domain}/config-service/admin"
`
    return new File([content], "setup.sh", {type: "text/plain"});
}

export const generateFile = (values: FormValues) => {


    const envFile = createEnvFile(values);
    const composeFile = createComposeFile();
    const bashFile = createBashScriptFile(values);

    const zip = new JSZip();
    zip.file(envFile.name, envFile);
    zip.file(composeFile.name, composeFile);
    zip.file(bashFile.name, bashFile);

    zip.generateAsync({type: "blob"}).then(function (content) {
        const link = document.createElement("a");

        link.href = URL.createObjectURL(content);
        link.download = "bot.zip";

        document.body.appendChild(link);

        link.click();

        document.body.removeChild(link);
    });
}

