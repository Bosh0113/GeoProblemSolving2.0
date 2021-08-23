<script>
    export default {
        methods: {
            notification(title, icon, body, tag = 'tag', duration = 5000) {
                if (window.Notification && window.Notification.permission === 'granted') {
                    const n = new window.Notification(title, {
                        icon,
                        body,
                        tag
                    },);
                    n.onclick = function () {
                        window.focus();
                        this.close();
                    };
                    setTimeout(n.close.bind(n), duration);
                }
            }
        }
    }
</script>