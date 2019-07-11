var scene, camera, renderer,controls;
var stats, container;


$(document).ready(function () {

    container = document.getElementById("canvas");

    if (Detector.webgl) {
        // Initiate function or other initializations here
        initThree();
        initScene();
        initCamera();
        initOrbitCtrl();
        initLight();
        initPlane();
        animate();
    } else {
        var warning = Detector.getWebGLErrorMessage();
        document.getElementById('canvas').appendChild(warning);
    }

});

function initThree() {
    renderer = new THREE.WebGLRenderer();
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize(window.innerWidth, window.innerHeight);
    container.appendChild(renderer.domElement);


    stats = new Stats();
    stats.domElement.style.position = 'absolute';
    stats.domElement.style.top = (window.innerHeight - 48) + "px";
    stats.domElement.style.left = '0px';
    stats.domElement.style.bottom = '0px';
    container.appendChild(stats.domElement);


    window.addEventListener('resize', onWindowResize, false);
}

function initScene() {
    scene = new THREE.Scene();
    scene.background = new THREE.Color(0xf0f0f0);
}

function initCamera() {
    camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.1, 2000);
    camera.position.z = 100;
    camera.position.y = 50;
    camera.lookAt(new THREE.Vector3(0, 0, 0));

}

function initOrbitCtrl(){
    controls = new THREE.OrbitControls( camera, renderer.domElement );

    //controls.addEventListener( 'change', render ); // call this only in static scenes (i.e., if there is no animation loop)

    controls.enableDamping = true; // an animation loop is required when either damping or auto-rotation are enabled
    controls.dampingFactor = 0.5;

    controls.screenSpacePanning = false;

    controls.minDistance = 10;
    controls.maxDistance = 2000;

    controls.maxPolarAngle = Math.PI;

}

function initLight() {
    var ambientLight = new THREE.AmbientLight(0xFFFFFF);
    scene.add(ambientLight);
}

function initPlane() {
    var gridHelper = new THREE.GridHelper(100, 20);
    scene.add(gridHelper);
}

function animate() {
    requestAnimationFrame(animate);

    stats.begin();
    render();
    stats.end();
}

function render() {
    // changeCamera();
    controls.update();
    renderer.render(scene, camera);
}

function onWindowResize() {
    var aspect = window.innerWidth / window.innerHeight;
    camera.aspect = aspect;

    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight);

    stats.domElement.style.position = 'absolute';
    stats.domElement.style.top = (window.innerHeight - 48) + "px";
    stats.domElement.style.left = '0px';
    stats.domElement.style.bottom = '0px';
}