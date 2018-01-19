<script type="text/javascript">

  const userSexList = [{code:0,name:'男'},{code:1,name:'女'},{code:2,name:'未知'}];

  const userStatusList = [{code:1,name:'活动'},{code:2,name:'禁用'},{code:3,name:'注销'}];

  function getRandColor () {
    var tem = Math.round(Math.random() * colorListLength)
    return colorList[tem]
  }
  export default{
    getRandColor,
    userStatusList,
    userSexList
  }
</script>
