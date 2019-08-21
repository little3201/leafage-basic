package top.abeille.basic.assets.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.abeille.basic.assets.document.Article;
import top.abeille.basic.assets.repository.ArticleRepository;

/**
 * 文章接口测试
 *
 * @author liwenqiang 2019-08-20 22:38
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleInfoServiceImplTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void getByArticleId() {
    }

    @Test
    public void saveArticle() {
        Article article = new Article();
        article.setArticleId("002");
        String content = "一步步带你实现web全景看房——three.js\n" +
                "\n" +
                "    canvas画2d相信大家都很熟悉了，但3d世界更加炫酷。我们直接从three.js入手。下面我们从0开始来摸索一下3d世界\n" +
                "\n" +
                "1. 基本概念\n" +
                "\n" +
                "在THREEjs中，渲染一个3d世界的必要因素是场景（scene）、相机（camera）、渲染器（renderer）。渲染出一个3d世界后，可以往里面增加各种各样的物体、光源等，形成一个3d世界：\n" +
                "\n" +
                "    场景:右手坐标系,一切要素都在场景里面，相当于“世界”，包括各种物质和物理变化\n" +
                "\n" +
                "// 创建场景\n" +
                "const scene = new THREE.Scene();\n" +
                "复制代码\n" +
                "\n" +
                "    照相机:摄像机就相当于人眼，有摄像机才可以看见场景里面的一切物体和光源。常用的是正交摄像机和透视摄像机\n" +
                "\n" +
                "正交摄像机是一个矩形可视区域，物体只有在这个区域内才是可见的物体无论距离摄像机是远或事近，物体都会被渲染成一个大小。一般应用场景是2.5d游戏如跳一跳、机械模型\n" +
                "\n" +
                "// 创建正交相机\n" +
                "const camera = new THREE.OrthographicCamera(\n" +
                "    -window.innerWidth / 200,\n" +
                "    window.innerWidth /200 ,\n" +
                "    window.innerHeight/ 200,\n" +
                "    -window.innerHeight/ 200,\n" +
                "    1,\n" +
                "    1000\n" +
                ");\n" +
                "复制代码\n" +
                "\n" +
                "我们可以看见上图的效果，有一个正方体已经走了很远但是大小不变。另外还可以看见角落有一个正方体已经被截断了一部分，那是因为正交摄像机仅仅展示一个空间内的场景，所以会有截断效果。\n" +
                "\n" +
                "透视摄像机是最常用的摄像机类型，模拟人眼的视觉，近大远小（透视）。Fov表示的是视角，Fov越大，表示眼睛睁得越大，离得越远，看得更多。如果是需要模拟现实，基本都是用这个相机\n" +
                "\n" +
                "// 创建透视相机\n" +
                "const camera = new THREE.PerspectiveCamera(\n" +
                "          90,\n" +
                "          window.innerWidth / window.innerHeight,\n" +
                "          1,\n" +
                "          10000\n" +
                "        );\n" +
                "复制代码\n" +
                "\n" +
                "近大远小的效果就出来了，比较符合现实\n" +
                "\n" +
                "    渲染器\n" +
                "\n" +
                "最后需要把所有的内容渲染到页面上，需要一个渲染器：\n" +
                "\n" +
                "      const renderer = new THREE.WebGLRenderer();\n" +
                "      renderer.setSize(window.innerWidth, window.innerHeight); // canvas大小\n" +
                "      document.body.appendChild(renderer.domElement);\n" +
                "\n" +
                "复制代码\n" +
                "\n" +
                "2. 给画面增加内容\n" +
                "\n" +
                "上面的确是把3d世界画出来了，只是没有什么东西。在three.js中，我们需要增加光源和mesh\n" +
                "mesh\n" +
                "\n" +
                "mesh即是网格。在计算机里，3D世界是由点组成的，无数的面拼接成各种形状的物体。这种模型叫做网格模型。一条线是两个点组成，一个面是3个点组成，一个物体由多个3点组成的面组成：\n" +
                "\n" +
                "而网格（mesh）又是由几何体（geometry）和材质（material）构成的\n" +
                "geometry\n" +
                "\n" +
                "我们所能想象到的几何体，框架都自带了，我们只需要调用对应的几何体构造函数即可创建。几何体的创建方法都是new，如BoxBuffer：\n" +
                "\n" +
                "const geometry = new THREE.BoxBufferGeometry( 1, 1, 1 );\n" +
                "复制代码\n" +
                "\n" +
                "创建的时候，一般定义了渲染一个 3D 物体所需要的基本数据：Face 面、Vertex 顶点等信息。THREE.xxxGeometry指的是框架自带的几何体，不同几何体所需要的参数有所不同，大概是width、height、radius、depth、segment、detail、angle等属性\n" +
                "\n" +
                "更多geometry相关api\n" +
                "\n" +
                "    BufferGeometry和Geometry有什么不同?就实现的效果来说它们都是一样，但是BufferGeometry的多了一些顶点属性，且性能较好。对于开发者来说，Geometry对象属性少体验更好。THREE解析几何体对象的时候，如果是Geometry，则会把对象转换成ufferGeometry对象，再进行下一步渲染\n" +
                "\n" +
                "material\n" +
                "\n" +
                "一个物体很多的物理性质，取决于其材料，材料也决定了几何体的外表。材料的创建方法也是new，如Lambert材料：\n" +
                "\n" +
                "const material = new THREE.MeshLambertMaterial();\n" +
                "复制代码\n" +
                "\n" +
                "一个物体是否有镜面感、亮暗、颜色、透明、是否反光等性质，取决于使用什么材料。THREE.xxxMaterial指的是框架自带的材料，不同材料所需要的参数也是有所不同\n" +
                "\n" +
                "更多material相关api\n" +
                "\n" +
                "有了geometry和material,就可以创建一个mesh并追加到场景中：\n" +
                "\n" +
                "const mesh = new THREE.Mesh(geometry, material);\n" +
                "scene.add(mesh);\n" +
                "复制代码\n" +
                "\n" +
                "光源\n" +
                "\n" +
                "一个3d世界，如果需要更加逼真，那就需要光源了。光也有很多种，常见的有平行光（图2）、点光源（图3）、环境光（环境光充满所有的几何体表面）、聚光灯（图1）\n" +
                "\n" +
                "其中，只有平行光、点光源才能产生阴影。而且有的材料是受光源影响，没有光就是黑的。而一些材料是不受光影响的。光源的创建，如直射光：\n" +
                "\n" +
                "const light = new THREE.DirectionalLight(0xffffff, 0.9)\n" +
                "复制代码\n" +
                "\n" +
                "THREE.xxxLight指的是框架自带的光源构造函数，一般实例化的时候需要的参数是color、intensity、distance等配置。另外，一个3d世界当然不是一种光构成，所以光可以叠加，叠加的结果作用与物体上。\n" +
                "\n" +
                "而且物体的影子也不是白送的，需要某些支持影子的光加上开发者配置：\n" +
                "\n" +
                "// 光产生影子\n" +
                "light.castShadow = true;\n" +
                "// 地面接受影子\n" +
                "ground.receiveShadow = true;\n" +
                "// 物体产生影子\n" +
                "mesh.castShadow = true;\n" +
                "复制代码\n" +
                "\n" +
                "更多光源相关的api\n" +
                "\n" +
                "更多影子相关的api\n" +
                "3. 调试工具\n" +
                "轨道控制器\n" +
                "\n" +
                "加上此控制器，就可以通过鼠标拖拽、滚动对整个画面进行拖拽放缩 轨道控制器代码在THREE官方github上，如果使用的时候报错THREE.OrbitControls is not a constructor，那么就copy一份下来，第一行加一个window：window.THREE.OrbitControls = ...\n" +
                "\n" +
                "使用方法就是new一个控制器，然后监听变化，触发render\n" +
                "\n" +
                "        const controls = new THREE.OrbitControls(camera, renderer.domElement);\n" +
                "        controls.addEventListener(\"change\", () => {\n" +
                "          renderer.render(scene, camera);\n" +
                "        });\n" +
                "        controls.minDistance = 1;\n" +
                "        controls.maxDistance = 2000;\n" +
                "        controls.enablePan = false;\n" +
                "复制代码\n" +
                "\n" +
                "性能监控\n" +
                "\n" +
                "源代码。可以拷贝下来，挂在window上\n" +
                "\n" +
                "官方大部分例子都使用了一个stat的插件，在左上角会出现性能变化的曲线，供我们调试使用。使用方法：\n" +
                "\n" +
                "    const stat = new Stats();\n" +
                "    document.body.appendChild(stat.dom);\n" +
                "    \n" +
                "    // 改造render函数\n" +
                "    function render() {\n" +
                "      renderer.render(scene, camera);\n" +
                "      stat.update();\n" +
                "    }\n" +
                "复制代码\n" +
                "\n" +
                "4. let's coding\n" +
                "\n" +
                "先把场景、摄像机、渲染器弄出来，然后添加一个红色的球\n" +
                "\n" +
                "      function init() {\n" +
                "      const renderer = new THREE.WebGLRenderer();\n" +
                "      renderer.setPixelRatio(window.devicePixelRatio);\n" +
                "      renderer.setSize(window.innerWidth, window.innerHeight);\n" +
                "      document.body.appendChild(renderer.domElement);\n" +
                "\n" +
                "      // 场景\n" +
                "      const scene = new THREE.Scene();\n" +
                "      // 相机\n" +
                "      const camera = new THREE.PerspectiveCamera(\n" +
                "        90,\n" +
                "        window.innerWidth / window.innerHeight,\n" +
                "        0.1,\n" +
                "        100\n" +
                "      );\n" +
                "      camera.position.set(10, 0, 0);\n" +
                "\n" +
                "      // 轨道控制器\n" +
                "      const controls = new THREE.OrbitControls(camera, renderer.domElement);\n" +
                "      controls.addEventListener(\"change\", render);\n" +
                "      controls.minDistance = 1;\n" +
                "      controls.maxDistance = 200;\n" +
                "      controls.enablePan = false;\n" +
                "\n" +
                "      // 新增一个红色球\n" +
                "      const geometry = new THREE.SphereGeometry(1, 10, 10);\n" +
                "      const material = new THREE.MeshBasicMaterial({ color: 0xff0000 });\n" +
                "      const mesh = new THREE.Mesh(geometry, material);\n" +
                "      scene.add(mesh);\n" +
                "      // 坐标轴辅助线\n" +
                "      scene.add(new THREE.AxisHelper(1000));\n" +
                "\n" +
                "      controls.update(); // 控制器需要\n" +
                "      controls.target.copy(mesh.position);\n" +
                "\n" +
                "      function render() {\n" +
                "        renderer.render(scene, camera);\n" +
                "      }\n" +
                "\n" +
                "      function r() {\n" +
                "        render();\n" +
                "        requestAnimationFrame(r)\n" +
                "      }\n" +
                "      r()\n" +
                "    }\n" +
                "    \n" +
                "    init();\n" +
                "复制代码\n" +
                "\n" +
                "此时，可以看见坐标原点上有一个球。其实，一个几何体纹理是可以使用图片的，甚至还可以使用视频，此时不能双击打开html，需要本地起一个服务器打开。我们改造一下mesh:\n" +
                "\n" +
                "    function addImg(url, scene, n = 1) {\n" +
                "      const texture = THREE.ImageUtils.loadTexture(url);\n" +
                "      const material = new THREE.MeshBasicMaterial({ map: texture });\n" +
                "      const geometry = new THREE.SphereGeometry(1, 10, 10);\n" +
                "      const mesh = new THREE.Mesh(geometry, material);\n" +
                "      scene.add(mesh);\n" +
                "      return mesh;\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "      // const geometry = new THREE.SphereGeometry(1, 10, 10);\n" +
                "      // const material = new THREE.MeshBasicMaterial({ color: 0xff0000 });\n" +
                "      // const mesh = new THREE.Mesh(geometry, material);\n" +
                "      // 去酷家乐找了一个图\n" +
                "      const mesh = addImg(\"https://qhyxpicoss.kujiale.com/r/2019/07/01/L3D137S8ENDIADDWAYUI5L7GLUF3P3WS888_3000x4000.jpg?x-oss-process=image/resize,m_fill,w_1600,h_920/format,webp\", scene, 1); \n" +
                "      scene.add(mesh);\n" +
                "复制代码\n" +
                "\n" +
                "原点显示一个图作为纹理的球\n" +
                "\n" +
                "    基本都ok了，怎么实现全景看房呢？我们上面的条件都ok了，最后需要做的事情是：将摄像机放在球体中心、轨道控制器放缩上限最小最大设置成1和2、渲染mesh内表面\n" +
                "\n" +
                "    // 调整max\n" +
                "      controls.minDistance = 1;\n" +
                "      // controls.maxDistance = 200;\n" +
                "      controls.maxDistance = 2;\n" +
                "      \n" +
                "      // 调整球大小\n" +
                "      // const geometry = new THREE.SphereGeometry(1, 10, 10);\n" +
                "      const geometry = new THREE.SphereGeometry(50, 256, 256);\n" +
                "      \n" +
                "      // 摄像机放球体中心\n" +
                "      // camera.position.set(10, 0, 0);\n" +
                "      camera.position.set(-0.3, 0, 0);\n" +
                "      \n" +
                "      // 渲染球体的双面\n" +
                "      const material = new THREE.MeshLambertMaterial({ map: texture });\n" +
                "      material.side = THREE.DoubleSide;\n" +
                "复制代码\n" +
                "\n" +
                "全景看房的效果就出来了，然后只需拖动就可以调整角度了。引入是普通平面图，所以图的首尾交接有一点问题。\n" +
                "\n" +
                "    这只是实现的一个思路，实现的方法有很多，如柱体、立方体，图片可能是扇形的全景图也可能是多个图片拼接起来的。具体的细节根据业务进行调整\n" +
                "\n" +
                "全部代码如下，需要引入three.js、orbitcontrol";
        article.setArticleContent(content);
        articleRepository.save(article);
    }
}
