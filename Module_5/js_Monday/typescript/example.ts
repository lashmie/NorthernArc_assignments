type employee = {
 name: string;
 department: string;
};
type player = {
 team: string;
};
const gopi:employee={
name: "Gopi Sir",
department:"Full stack"

}
console.log(gopi);
type CombinedType = employee & player;
const sachin: CombinedType = {
 name: "sachin",
 department: "coding",
 team: "angular giants"
};
console.log(sachin);